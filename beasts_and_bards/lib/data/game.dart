import 'package:cloud_firestore/cloud_firestore.dart';

class Game {
  Game(
      {required this.name,
      required this.playersId,
      required this.gameId,
      this.dm = "",
      this.active = false});
  final String name;
  final List<String> playersId;
  final String gameId;
  final String dm;
  final bool active;

  factory Game.fromFirestore(
    DocumentSnapshot<Map<String, dynamic>> snapshot,
    SnapshotOptions? options,
  ) {
    final data = snapshot.data();
    return Game(
      name: data?['name'],
      playersId: data?['playersId'] is Iterable
          ? List.from(data?['playersId'])
          : List<String>.empty(growable: true),
      gameId: data?['gameId'],
      dm: data?['dm'],
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      "gameId": gameId,
      "dm": dm,
      "name": name,
      "active": active,
      "playersId": List<String>.from(playersId),
    };
  }
}
