import 'package:pocketbase/pocketbase.dart';

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

  factory Abilities.fromJson(Map<String, dynamic> m) {
    int v(String k) => (m[k] as num?)?.toInt() ?? 0;
    return Abilities(
        charisma: v('charisma'),
        constitution: v('constitution'),
        dexterity: v('dexterity'),
        intelligence: v('intelligence'),
        strength: v('strength'),
        wisdom: v('wisdom'));
  }

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

  factory Character.fromRecord(RecordModel r) {
    final abilities = r.data['abilities'];
    return Character(
      name: r.getStringValue('name'),
      race: r.getStringValue('race'),
      abilities: Abilities.fromJson(
          abilities is Map ? Map<String, dynamic>.from(abilities) : {}),
      gameId: r.getStringValue('game'),
      uuid: r.id,
      active: r.getBoolValue('active'),
    );
  }

  /// Body for creating the record. `owner` is added by `ApplicationState`.
  Map<String, dynamic> toJson() {
    return {
      "name": name,
      "race": race,
      "abilities": abilities.toJson(),
      "game": gameId,
      "active": active,
    };
  }
}
