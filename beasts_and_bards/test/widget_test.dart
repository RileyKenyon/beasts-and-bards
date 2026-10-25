import 'package:beasts_and_bards/data/character.dart';
import 'package:beasts_and_bards/data/game.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:pocketbase/pocketbase.dart';

void main() {
  test('Game.fromRecord reads fields and the expanded dm name', () {
    final game = Game.fromRecord(RecordModel.fromJson({
      'id': 'g1',
      'name': 'Quest',
      'active': true,
      'players': ['u2', 'u3'],
      'dm': 'u1',
      'expand': {
        'dm': {'id': 'u1', 'name': 'Merlin'}
      },
    }));
    expect(game.gameId, 'g1');
    expect(game.name, 'Quest');
    expect(game.dm, 'Merlin');
    expect(game.playersId, ['u2', 'u3']);
    expect(game.active, isTrue);
    expect(game.toJson(), {
      'name': 'Quest',
      'active': true,
      'players': ['u2', 'u3'],
    });
  });

  test('Character round-trips through a record', () {
    final character = Character(
      name: 'Bob',
      race: 'Elf',
      abilities: Abilities(
          charisma: 1,
          constitution: 2,
          dexterity: 3,
          intelligence: 4,
          strength: 5,
          wisdom: 6),
      gameId: 'g1',
      uuid: '',
    );
    final back = Character.fromRecord(RecordModel.fromJson({
      'id': 'c1',
      ...character.toJson(),
    }));
    expect(back.uuid, 'c1');
    expect(back.gameId, 'g1');
    expect(back.abilities.toJson(), character.abilities.toJson());
  });
}
