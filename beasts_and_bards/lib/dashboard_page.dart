import 'package:beasts_and_bards/data/game.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

import 'app_state.dart';
import 'src/widgets.dart';

class DashboardPage extends StatefulWidget {
  const DashboardPage({super.key, required this.appState});
  final ApplicationState appState;

  @override
  State<DashboardPage> createState() => _DashboardPage();
}

class _DashboardPage extends State<DashboardPage> {
  @override
  void dispose() {
    super.dispose();
  }

  final Stream<QuerySnapshot<Map<String, dynamic>>> _dashboardStream =
      FirebaseFirestore.instance
          .collection('games')
          // .withConverter(
          //     fromFirestore: Game.fromFirestore,
          //     toFirestore: (Game game, options) => game.toFirestore())
          .snapshots();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            const DrawerHeader(
              decoration: BoxDecoration(color: Colors.red),
              child: Image(image: AssetImage('assets/d20.png')),
            ),
            ListTile(
              title: const Text('Go Home'),
              onTap: () => (context.go('/')),
            ),
            ListTile(
              title: const Text('Profile'),
              onTap: () => (context.go('/profile')),
            )
          ],
        ),
      ),
      appBar: AppBar(title: const Text("Dashboard")),
      body: StreamBuilder<QuerySnapshot<Map<String, dynamic>>>(
        stream: _dashboardStream,
        builder: (context,
            AsyncSnapshot<QuerySnapshot<Map<String, dynamic>>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const CircularProgressIndicator();
          }

          if (snapshot.hasError || !snapshot.hasData) {
            return const Text('Oops something went wrong');
          }

          if (snapshot.data == null) {
            return const Text('Create a game to get started!');
          }
          return ListView(
            children: snapshot.data!.docs
                .map((DocumentSnapshot<Map<String, dynamic>> document) {
                  // @TODO figure out how to use the Firestore instance withconverter instead
                  SnapshotOptions? options;
                  final data = Game.fromFirestore(document, options);
                  return DashboardListItem(
                      game: data,
                      onTap: () =>
                          {context.push('/game-detail', extra: data.gameId)});
                })
                .toList()
                .cast(),
          );
        },
      ),
      floatingActionButton: ExpandableFab(
        distance: 75.0,
        children: [
          ActionButton(
            onPressed: () => context.push('/create-game'),
            icon: const Icon(Icons.add),
          ),
          ActionButton(
            onPressed: () => context.push('/friends-list'),
            icon: const Icon(Icons.people),
          ),
        ],
      ),
    );
  }
}
