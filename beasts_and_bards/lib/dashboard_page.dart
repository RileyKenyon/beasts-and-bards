import 'package:beasts_and_bards/data/game.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:pocketbase/pocketbase.dart';

import 'app_state.dart';
import 'pb.dart';
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

  // Server rules only return the games the user takes part in.
  final Stream<List<RecordModel>> _dashboardStream =
      watch('games', expand: 'dm', sort: '-created');

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
      body: StreamBuilder<List<RecordModel>>(
        stream: _dashboardStream,
        builder: (context, AsyncSnapshot<List<RecordModel>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const CircularProgressIndicator();
          }

          if (snapshot.hasError || !snapshot.hasData) {
            return const Text('Oops something went wrong');
          }

          if (snapshot.data!.isEmpty) {
            return const Text('Create a game to get started!');
          }
          return ListView(
            children: snapshot.data!.map((record) {
              final game = Game.fromRecord(record);
              return DashboardListItem(
                  game: game,
                  onTap: () =>
                      context.push('/game-detail', extra: game.gameId));
            }).toList(),
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
