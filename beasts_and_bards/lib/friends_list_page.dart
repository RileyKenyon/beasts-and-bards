// Page that extends a StatefulWidget
// shows the friend list kept by ApplicationState
// Displays the friend name
// Button for sending friend request via form with email address

import 'package:flutter/material.dart';
import 'app_state.dart';

class FriendsListPage extends StatelessWidget {
  const FriendsListPage({super.key, required this.appState});
  final ApplicationState appState;

  @override
  Widget build(BuildContext context) {
    final friends = appState.friendsList;
    return Scaffold(
      appBar: AppBar(title: const Text("Friends List")),
      body: friends.isEmpty
          ? const Text("No Friends")
          : ListView.builder(
              itemCount: friends.length,
              itemBuilder: (context, index) => Text(friends[index].name),
            ),
    );
  }
}
