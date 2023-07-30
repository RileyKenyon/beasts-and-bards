// Page that extends a StatefulWidget
// loads a friend list from firebase
// Displays the friend name
// Button for sending friend request via form with email address

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'app_state.dart';

class FriendsListPage extends StatefulWidget {
  const FriendsListPage({super.key, required this.appState});
  final ApplicationState appState;
  @override
  State<FriendsListPage> createState() => _FriendsListPageState();
}

class _FriendsListPageState extends State<FriendsListPage> {
  final emailTextController = TextEditingController();

  @override
  void dispose() {
    emailTextController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Friends List")),
      body: FirebaseAuth.instance.currentUser != null
          ? StreamBuilder(
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting ||
                    snapshot.connectionState == ConnectionState.none) {
                  return const CircularProgressIndicator();
                } else {
                  if (snapshot.hasData && snapshot.data != null) {
                    return ListView.builder(
                      itemBuilder: (context, index) {
                        return FutureBuilder(
                            builder: (context, snapshot) {
                              if (snapshot.connectionState ==
                                      ConnectionState.waiting ||
                                  snapshot.connectionState ==
                                      ConnectionState.none) {
                                return const CircularProgressIndicator();
                              } else {
                                return (snapshot.hasData &&
                                        snapshot.data != null)
                                    ? Text(snapshot.data!.get('name') as String)
                                    : Text("No Friends");
                              }
                            },
                            future: FirebaseFirestore.instance
                                .collection('users')
                                .doc(snapshot.data!.docs[index].id)
                                .get());
                      },
                      itemCount: snapshot.data!.docs.length,
                    );
                  } else {
                    return const Text("Unable to connect");
                  }
                }
              },
              stream: FirebaseFirestore.instance
                  .collection(
                      'users/${FirebaseAuth.instance.currentUser!.uid}/friends')
                  .snapshots(),
            )
          : const Text("No Friends"),
    );
  }
}
