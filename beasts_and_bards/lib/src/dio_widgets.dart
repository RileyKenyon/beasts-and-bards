import 'package:beasts_and_bards/src/widgets.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class DndManager extends StatefulWidget {
  const DndManager({super.key});

  @override
  State<DndManager> createState() => _DndManager();
}

class _DndManager extends State<DndManager> {
  final Future<List<Monster>> _future = getMonsters();
  @override
  Widget build(BuildContext context) {
    return Center(
      child: FutureBuilder<List<Monster>>(
        future: _future,
        builder: (context, snapshot) {
          if (snapshot.hasError) {
            return Text('Could not load monsters: ${snapshot.error}');
          }
          final monsters = snapshot.data;
          if (monsters == null) {
            return const CircularProgressIndicator();
          }
          return ListView.builder(
            itemCount: monsters.length,
            prototypeItem: const ListTile(title: Text("Prototype")),
            itemBuilder: (context, index) => ListTile(
              title: Text(monsters[index].name),
              onTap: () =>
                  context.push('/monster-detail', extra: monsters[index]),
            ),
          );
        },
      ),
    );
  }
}

class MonsterDetailsPage extends StatefulWidget {
  const MonsterDetailsPage({super.key, required this.monster});
  final Monster monster;

  @override
  State<MonsterDetailsPage> createState() => _MonsterDetailsPage();
}

class _MonsterDetailsPage extends State<MonsterDetailsPage> {
  late final Future<Monster> _future = getMonster(widget.monster.key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: FutureBuilder<Monster>(
        future: _future,
        builder: (context, snapshot) {
          final Widget child;
          if (snapshot.hasError) {
            child = Text('Could not load monster: ${snapshot.error}');
          } else if (snapshot.hasData) {
            child = MonsterWidget(monster: snapshot.data!);
          } else {
            child = const CircularProgressIndicator();
          }
          return Center(child: child);
        },
      ),
    );
  }
}

class Monster {
  Monster({
    required this.key,
    required this.name,
    this.type,
    this.size,
    this.armorClass,
    this.hitPoints,
    this.challengeRating,
    this.abilityScores = const {},
  });
  final String key;
  final String name;
  final String? type;
  final String? size;
  final int? armorClass;
  final int? hitPoints;
  final num? challengeRating;

  /// Lower-case ability name (`strength`, ...) to score.
  final Map<String, int> abilityScores;

  factory Monster.fromJson(Map<String, dynamic> json) {
    final scores = json['ability_scores'];
    return Monster(
      key: json['key'] as String,
      name: json['name'] as String,
      type: json['type']?['name'] as String?,
      size: json['size']?['name'] as String?,
      armorClass: json['armor_class'] as int?,
      hitPoints: json['hit_points'] as int?,
      challengeRating: json['challenge_rating'] as num?,
      abilityScores: scores is Map
          ? {for (final e in scores.entries) e.key as String: e.value as int}
          : const {},
    );
  }

  /// 0.25 -> "1/4", 10.0 -> "10".
  String? get challengeRatingLabel {
    final cr = challengeRating;
    if (cr == null) return null;
    return switch (cr) {
      0.125 => '1/8',
      0.25 => '1/4',
      0.5 => '1/2',
      _ => cr.toInt().toString(),
    };
  }
}

final _dio = Dio(BaseOptions(baseUrl: 'https://api.open5e.com/v2'));

/// SRD 2024 creatures, names only.
// shortcut: one page of 500 (SRD 2024 has 331), follow `next` if it outgrows that.
Future<List<Monster>> getMonsters() async {
  final response = await _dio.get('/creatures/', queryParameters: {
    'document__key': 'srd-2024',
    'fields': 'key,name',
    'limit': 500,
    'format': 'json',
  });
  return [for (final m in response.data['results']) Monster.fromJson(m)];
}

Future<Monster> getMonster(String key) async {
  final response = await _dio.get('/creatures/$key/', queryParameters: {
    'fields': 'key,name,type,size,armor_class,hit_points,challenge_rating,'
        'ability_scores',
    'format': 'json',
  });
  return Monster.fromJson(response.data);
}
