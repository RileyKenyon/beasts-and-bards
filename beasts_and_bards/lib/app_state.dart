// Copyright 2022 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:pocketbase/pocketbase.dart';

import 'data/character.dart';
import 'data/friend.dart';
import 'data/game.dart';
import 'pb.dart';

class ApplicationState extends ChangeNotifier {
  ApplicationState() {
    _authSubscription = pb.authStore.onChange.listen((_) => _onAuthChanged());
    _onAuthChanged();
  }

  StreamSubscription<AuthStoreEvent>? _authSubscription;
  StreamSubscription<List<RecordModel>>? _friendsListSubscription;

  RecordModel? get _user =>
      pb.authStore.isValid && pb.authStore.model is RecordModel
          ? pb.authStore.model as RecordModel
          : null;

  bool get loggedIn => _user != null;

  bool get emailVerified => _user?.getBoolValue('verified') ?? false;

  String get userId => _user?.id ?? '';

  String get email => _user?.getStringValue('email') ?? '';

  String get userName => _user?.getStringValue('name') ?? '';

  List<Friend> _friendsList = [];
  List<Friend> get friendsList => _friendsList;

  String activeGameId = "";

  void _onAuthChanged() {
    _friendsListSubscription?.cancel();
    _friendsListSubscription = null;
    _friendsList = [];
    if (loggedIn) {
      _friendsListSubscription =
          watch('friends', filter: 'owner = "$userId"', expand: 'friend')
              .listen((records) {
        _friendsList = [
          for (final r in records)
            if (r.expand['friend']?.isNotEmpty ?? false)
              Friend(
                  name: r.expand['friend']!.first.getStringValue('name'),
                  message: ""),
        ];
        notifyListeners();
      }, onError: (Object _) {});
    }
    notifyListeners();
  }

  @override
  void dispose() {
    _authSubscription?.cancel();
    _friendsListSubscription?.cancel();
    super.dispose();
  }

  Future<void> signIn(String email, String password) async {
    await pb.collection('users').authWithPassword(email, password);
  }

  /// Creates the account, sends the verification mail and signs in.
  Future<void> register(String email, String password) async {
    await pb.collection('users').create(body: {
      'email': email,
      'password': password,
      'passwordConfirm': password,
      'name': email.split('@')[0],
    });
    await pb.collection('users').requestVerification(email);
    await signIn(email, password);
  }

  Future<void> requestPasswordReset(String email) =>
      pb.collection('users').requestPasswordReset(email);

  void signOut() => pb.authStore.clear();

  Future<void> refreshLoggedInUser() async {
    if (!loggedIn) return;
    await pb.collection('users').authRefresh();
  }

  Future<void> addGameToDatabase(Game newgame) {
    if (!loggedIn) {
      throw Exception('Must be logged in');
    }
    return pb
        .collection('games')
        .create(body: {...newgame.toJson(), 'dm': userId});
  }

  /// Creates [character] for the current user and returns it with its
  /// server-assigned id in `uuid`.
  Future<Character> addCharacterToDatabase(Character character) async {
    if (!loggedIn) {
      throw Exception('Must be logged in');
    }
    final record = await pb
        .collection('characters')
        .create(body: {...character.toJson(), 'owner': userId});
    return Character.fromRecord(record);
  }
}
