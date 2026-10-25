import 'package:beasts_and_bards/src/dio_widgets.dart';
import 'package:flutter_test/flutter_test.dart';

void main() {
  test('Monster.fromJson reads an Open5e creature', () {
    final m = Monster.fromJson({
      'key': 'srd-2024_aboleth',
      'name': 'Aboleth',
      'type': {'name': 'Aberration', 'key': 'aberration'},
      'size': {'name': 'Large', 'key': 'large'},
      'challenge_rating': 10.0,
      'armor_class': 17,
      'hit_points': 150,
      'ability_scores': {
        'strength': 21,
        'dexterity': 9,
        'constitution': 15,
        'intelligence': 18,
        'wisdom': 15,
        'charisma': 18,
      },
    });
    expect(m.key, 'srd-2024_aboleth');
    expect(m.type, 'Aberration');
    expect(m.size, 'Large');
    expect(m.armorClass, 17);
    expect(m.hitPoints, 150);
    expect(m.challengeRatingLabel, '10');
    expect(m.abilityScores['charisma'], 18);
  });

  test('list entries with only key and name parse', () {
    final m = Monster.fromJson({'key': 'srd-2024_zombie', 'name': 'Zombie'});
    expect(m.name, 'Zombie');
    expect(m.armorClass, isNull);
    expect(m.abilityScores, isEmpty);
  });

  test('fractional challenge ratings are formatted', () {
    String? label(num cr) =>
        Monster(key: 'k', name: 'n', challengeRating: cr).challengeRatingLabel;
    expect(label(0.125), '1/8');
    expect(label(0.25), '1/4');
    expect(label(0.5), '1/2');
    expect(label(0), '0');
    expect(label(30), '30');
  });
}
