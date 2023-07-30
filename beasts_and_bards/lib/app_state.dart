// Copyright 2022 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';
import 'dart:math';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart'
    hide EmailAuthProvider, PhoneAuthProvider;
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_ui_auth/firebase_ui_auth.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:uuid/uuid.dart';
import 'data/friend.dart';
import 'data/game.dart';
import 'firebase_options.dart';
import 'dart:io';
import 'data/character.dart';

class ApplicationState extends ChangeNotifier {
  ApplicationState() {
    init();
  }

  bool _loggedIn = false;

  bool get loggedIn => _loggedIn;

  bool _emailVerified = false;

  bool get emailVerified => _emailVerified;

  Uuid uuidGen = const Uuid();

  // StreamSubscription<QuerySnapshot>? _databaseSubscription;
  StreamSubscription<QuerySnapshot>? _friendsListSubscription;
  List<Friend> _friendsList = [];
  List<Friend> get friendsList => _friendsList;

  // Add game list for user
  List<Game> _gameList = [];
  List<Game> get gameList => _gameList;
  String activeGameId = "";

  Future<void> init() async {
    await Firebase.initializeApp(
        options: DefaultFirebaseOptions.currentPlatform);
    if (kDebugMode) {
      await FirebaseAuth.instance.useAuthEmulator('localhost', 9099);
      FirebaseFirestore.instance.useFirestoreEmulator('localhost', 8080);
    }
    FirebaseUIAuth.configureProviders([
      EmailAuthProvider(),
    ]);

    if (loggedIn && FirebaseAuth.instance.currentUser != null) {
      _friendsListSubscription = FirebaseFirestore.instance
          .collection('users')
          .doc(FirebaseAuth.instance.currentUser?.uid)
          .collection('friendslist')
          .snapshots()
          .listen((snapshot) {
        _friendsList = [];
        for (final friend in snapshot.docs) {
          if (friend.data()['name'] != null) {
            _friendsList.add(
              Friend(
                name: friend.data()['name'] as String,
                message: "",
              ),
            );
          }
        }
        notifyListeners();
      });
      // _databaseSubscription = FirebaseFirestore.instance
      //     .collection('games')
      //     .snapshots()
      //     .listen((snapshot) {
      //   _gameList = [];
      //   for (final doc in snapshot.docs) {
      //     List<Friend> players = [];
      //     for (final f in doc.data()['players']) {
      //       players.add(Friend(name: f, message: ""));
      //     }
      //     _gameList.add(Game(
      //         name: doc.data()['text'],
      //         players: players,
      //         gameId: doc.id,
      //         dm: doc.data()['name'],
      //         active: false));
      //   }
      //   stdout.writeln('Number of games ${snapshot.docs.length}');
      //   notifyListeners();
      // });
    }

    FirebaseAuth.instance.userChanges().listen((user) {
      if (user != null) {
        _loggedIn = true;
        _emailVerified = user.emailVerified;
        _friendsListSubscription = FirebaseFirestore.instance
            .collection('users')
            .doc(user.uid)
            .collection('friendslist')
            .snapshots()
            .listen((snapshot) {
          _friendsList = [];
          for (final friend in snapshot.docs) {
            if (friend.data()['name'] != null) {
              _friendsList.add(
                Friend(
                  name: friend.data()['name'] as String,
                  message: "",
                ),
              );
            }
          }
          notifyListeners();
        });
        // _databaseSubscription = FirebaseFirestore.instance
        //     .collection('games')
        //     .snapshots()
        //     .listen((snapshot) {
        //   _gameList = [];
        //   for (final doc in snapshot.docs) {
        //     List<Friend> players = [];
        //     if (doc.data()['players'] != null) {
        //       List<String> playerNames = List.from(doc.data()['players']);
        //       for (final f in playerNames) {
        //         players.add(Friend(name: f, message: ""));
        //       }
        //     }
        //     _gameList.add(
        //       Game(
        //           name: doc.data()['text'],
        //           players: players,
        //           gameId: doc.id,
        //           dm: doc.data()['name'],
        //           active: false),
        //     );
        //   }
        //   stdout.writeln('Number of games ${snapshot.docs.length}');
        //   notifyListeners();
        // });
      } else {
        _loggedIn = false;
        _emailVerified = false;
        // _databaseSubscription?.cancel();
      }
      notifyListeners();
    });
  }

  Future<void> refreshLoggedInUser() async {
    final currentUser = FirebaseAuth.instance.currentUser;

    if (currentUser == null) {
      return;
    }

    await currentUser.reload();
  }

  Future<DocumentReference> submitGameToDatabase(String message) {
    if (!_loggedIn) {
      throw Exception('Must be logged in');
    }

    return FirebaseFirestore.instance.collection('games').add(<String, dynamic>{
      'text': message,
      'timestamp': DateTime.now().millisecondsSinceEpoch,
      'name': FirebaseAuth.instance.currentUser!.displayName,
      'userId': FirebaseAuth.instance.currentUser!.uid,
    });
  }

  Future<void> addGameToDatabase(Game newgame) {
    if (!_loggedIn || FirebaseAuth.instance.currentUser == null) {
      throw Exception('Must be logged in');
    }

    // List<String> playerNames = [];
    // for (final player in newgame.players) {
    //   playerNames.add(player.name);
    // }

    DocumentReference ref = FirebaseFirestore.instance
        .collection('games')
        .withConverter(
            fromFirestore: Game.fromFirestore,
            toFirestore: (Game game, options) => game.toFirestore())
        .doc(newgame.gameId);

    FirebaseFirestore.instance
        .collection('users')
        .doc(FirebaseAuth.instance.currentUser!.uid)
        .collection('games')
        .doc(newgame.gameId)
        .set({'gameRef': ref});

    return ref.set(newgame);
  }

  Future<void> addCharacterToActiveGame(Character character) {
    if (!_loggedIn || FirebaseAuth.instance.currentUser == null) {
      throw Exception('Must be logged in');
    }
    DocumentReference db =
        FirebaseFirestore.instance.collection('games').doc(activeGameId);
    return db.update({
      'playersId': FieldValue.arrayUnion([character.uuid])
      // 'players': [character.name],
    });
  }

  Future<void> addCharacterToDatabase(
      Character character, String gameId) async {
    if (!_loggedIn || FirebaseAuth.instance.currentUser == null) {
      throw Exception('Must be logged in');
    }

    DocumentReference ref = FirebaseFirestore.instance
        .collection('character')
        .withConverter(
            fromFirestore: Character.fromFirestore,
            toFirestore: (Character character, options) =>
                character.toFirestore())
        .doc(character.uuid);

    FirebaseFirestore.instance
        .collection('users')
        .doc(FirebaseAuth.instance.currentUser!.uid)
        .collection('characters')
        .add({'ref': ref});

    FirebaseFirestore.instance
        .collection('users')
        .doc(FirebaseAuth.instance.currentUser!.uid)
        .collection('games')
        .doc(gameId)
        .update({'characterRef': ref});

    return ref.set(character);
  }
}
