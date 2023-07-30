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
  final Future<Response<dynamic>> _future = getDndApiList("monsters");
  @override
  Widget build(BuildContext context) {
    return Center(
      child: FutureBuilder<Response<dynamic>>(
        future: _future,
        builder: (context, snapshot) {
          Widget child;
          if (snapshot.hasData &&
              snapshot.data != null &&
              snapshot.data?.statusCode == 200) {
            child = ListView.builder(
              itemCount: snapshot.data?.data['count'],
              prototypeItem: const ListTile(title: Text("Prototype")),
              itemBuilder: (context, index) {
                return ListTile(
                  title: Text(snapshot.data?.data['results'][index]['name']),
                  onTap: () => (context.push(
                    '/monster-detail',
                    extra: Monster(
                        url: snapshot.data?.data['results'][index]['url']
                            as String,
                        name: snapshot.data?.data['results'][index]['name']
                            as String,
                        imageUrl: ""),
                  )),
                );
              },
            );
          } else {
            child = const CircularProgressIndicator();
          }
          return Center(child: child);
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
  @override
  Widget build(BuildContext context) {
    final Future<Response<dynamic>> _future = getDndApi(widget.monster.url);
    return Scaffold(
      body: FutureBuilder<Response<dynamic>>(
        future: _future,
        builder: (context, snapshot) {
          Widget child;
          if (snapshot.hasData &&
              snapshot.data != null &&
              snapshot.data?.statusCode == 200) {
            final Monster m = Monster(
                url: snapshot.data?.data['url'],
                name: snapshot.data?.data['name'],
                imageUrl: snapshot.data?.data['image'] ?? "");
            child = MonsterWidget(monster: m);
          } else {
            child = const Icon(Icons.refresh);
          }
          return Center(child: child);
        },
      ),
    );
  }
}

class Monster {
  Monster({required this.url, required this.name, required this.imageUrl});
  final String name;
  final String url;
  final String imageUrl;
}

Future<Response<dynamic>> getDndApiList(String query) async {
  final dio = Dio();
  final response = await dio.get('https://www.dnd5eapi.co/api/${query}');
  return response;
}

Future<Response<dynamic>> getDndApi(String query) async {
  final dio = Dio();
  final response = await dio.get('https://www.dnd5eapi.co${query}');
  return response;
}

Image getDndApiImage(String query) {
  return Image.network('https://www.dnd5eapi.co$query');
}
