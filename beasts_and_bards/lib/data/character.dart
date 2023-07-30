import 'package:cloud_firestore/cloud_firestore.dart';

class Abilities {
  Abilities({
    required this.charisma,
    required this.constitution,
    required this.dexterity,
    required this.intelligence,
    required this.strength,
    required this.wisdom,
  });
  final int charisma;
  final int constitution;
  final int dexterity;
  final int intelligence;
  final int strength;
  final int wisdom;

  Map<String, dynamic> toJson() {
    return {
      'charisma': charisma,
      'constitution': constitution,
      'dexterity': dexterity,
      'intelligence': intelligence,
      'strength': strength,
      'wisdom': wisdom
    };
  }
}

class Character {
  Character({
    required this.name,
    required this.race,
    required this.abilities,
    required this.gameId,
    required this.uuid,
    this.active = false,
  });
  final String name;
  final String race;
  final Abilities abilities;
  final String gameId;
  final String uuid;
  final bool active;

  factory Character.fromFirestore(
    DocumentSnapshot<Map<String, dynamic>> snapshot,
    SnapshotOptions? options,
  ) {
    final data = snapshot.data();
    return Character(
      name: data?['name'],
      race: data?['race'],
      abilities: data?['abilities'] is Map
          ? getAbilitiesFromMap(Map.from(data?['abilities']))
          : Abilities(
              charisma: 0,
              constitution: 0,
              dexterity: 0,
              intelligence: 0,
              strength: 0,
              wisdom: 0),
      gameId: data?['gameId'],
      uuid: data?['uuid'],
      active: data?['active'],
    );
  }

  Map<String, dynamic> toFirestore() {
    return {
      "name": name,
      "race": race,
      "abilities": abilities.toJson(),
      "gameId": gameId,
      "uuid": uuid,
      "active": active,
    };
  }
}

Abilities getAbilitiesFromMap(Map<String, int> m) {
  return Abilities(
      charisma: m['charisma'] ?? 0,
      constitution: m['constitution'] ?? 0,
      dexterity: m['dexterity'] ?? 0,
      intelligence: m['intelligence'] ?? 0,
      strength: m['strength'] ?? 0,
      wisdom: m['wisdom'] ?? 0);
}
