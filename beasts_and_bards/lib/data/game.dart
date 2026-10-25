import 'package:pocketbase/pocketbase.dart';

class Game {
  Game(
      {required this.name,
      required this.playersId,
      required this.gameId,
      this.dm = "",
      this.active = false});
  final String name;

  /// User ids of the invited players.
  final List<String> playersId;
  final String gameId;

  /// Display name of the dungeon master (needs `expand: 'dm'`).
  final String dm;
  final bool active;

  factory Game.fromRecord(RecordModel r) {
    return Game(
      name: r.getStringValue('name'),
      playersId: r.getListValue<String>('players'),
      gameId: r.id,
      dm: r.expand['dm']?.first.getStringValue('name') ?? '',
      active: r.getBoolValue('active'),
    );
  }

  /// Body for creating the record. `dm` is added by `ApplicationState`.
  Map<String, dynamic> toJson() {
    return {
      "name": name,
      "active": active,
      "players": playersId,
    };
  }
}
