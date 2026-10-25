import 'dart:async';

import 'package:pocketbase/pocketbase.dart';
import 'package:shared_preferences/shared_preferences.dart';

/// PocketBase server, override with `--dart-define=PB_URL=...`
/// (Android emulator needs `http://10.0.2.2:8090`).
const _pbUrl =
    String.fromEnvironment('PB_URL', defaultValue: 'http://127.0.0.1:8090');

late final PocketBase pb;

/// Must be awaited before `runApp`: restores the persisted auth token.
Future<void> initPocketBase() async {
  final prefs = await SharedPreferences.getInstance();
  pb = PocketBase(
    _pbUrl,
    authStore: AsyncAuthStore(
      save: (data) async => prefs.setString('pb_auth', data),
      initial: prefs.getString('pb_auth'),
    ),
  );
}

/// Emits the full record list now and again after every change in
/// [collection]. Unsubscribes when the listener cancels.
Stream<List<RecordModel>> watch(
  String collection, {
  String? filter,
  String? expand,
  String? sort,
}) {
  late final StreamController<List<RecordModel>> controller;
  Future<UnsubscribeFunc>? subscription;

  Future<void> load() async {
    try {
      final records = await pb
          .collection(collection)
          .getFullList(filter: filter, expand: expand, sort: sort);
      if (!controller.isClosed) controller.add(records);
    } catch (e, s) {
      if (!controller.isClosed) controller.addError(e, s);
    }
  }

  controller = StreamController<List<RecordModel>>(
    onListen: () {
      load();
      subscription = pb.collection(collection).subscribe('*', (_) => load());
      subscription!.catchError((Object e, StackTrace s) {
        controller.addError(e, s);
        return () async {};
      });
    },
    onCancel: () async {
      final unsubscribe = await subscription?.catchError((_) => () async {});
      await unsubscribe?.call();
    },
  );
  return controller.stream;
}

/// Server message of a failed request, for showing to the user.
String errorMessage(Object e) {
  if (e is ClientException) {
    final msg = e.response['message'];
    if (msg is! String || msg.isEmpty) return 'Cannot reach server';
    final data = e.response['data'];
    final fields = data is Map
        ? [
            for (final entry in data.entries)
              '${entry.key}: ${entry.value is Map ? entry.value['message'] : entry.value}'
          ]
        : <String>[];
    return fields.isEmpty ? msg : '$msg ${fields.join(', ')}';
  }
  return e.toString();
}
