// Copyright 2022 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:beasts_and_bards/data/character.dart';
import 'package:beasts_and_bards/friends_list_page.dart';
import 'package:beasts_and_bards/new_character_form.dart';
import 'package:beasts_and_bards/nfc_reader.dart';
import 'package:beasts_and_bards/src/widgets.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';

import 'app_state.dart';
import 'auth_pages.dart';
import 'pb.dart';
import 'home_page.dart';
import 'dashboard_page.dart';
import 'create_game_form.dart';
import 'game_detail_page.dart';
import 'src/dio_widgets.dart';
import 'data/game.dart';
import 'nfc_notifier.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await initPocketBase();

  runApp(MultiProvider(
    providers: [
      ChangeNotifierProvider(
        create: (context) => ApplicationState(),
        // builder: ((context, child) => const App()),
      ),
      ChangeNotifierProvider(create: (context) => NfcModel())
    ],
    child: const App(),
  ));
}

final _router = GoRouter(
  routes: [
    GoRoute(
      path: '/',
      builder: (context, state) => const HomePage(),
      routes: [
        GoRoute(
          path: 'sign-in',
          builder: (context, state) => const SignInPage(),
          routes: [
            GoRoute(
              path: 'forgot-password',
              builder: (context, state) =>
                  ForgotPasswordPage(email: state.queryParams['email']),
            ),
          ],
        ),
        GoRoute(
          path: 'profile',
          builder: (context, state) => const ProfilePage(),
        ),
        GoRoute(
          path: 'dashboard',
          builder: (context, state) {
            return Consumer<ApplicationState>(
              builder: (context, appState, _) => DashboardPage(
                key: ValueKey(appState.loggedIn),
                appState: appState,
              ),
            );
          },
        ),
        GoRoute(
            path: 'create-game',
            builder: (context, state) {
              return Consumer<ApplicationState>(
                builder: (context, appState, _) => CreateGameForm(
                  key: ValueKey(appState.loggedIn),
                  appState: appState,
                ),
              );
            }),
        GoRoute(
            path: 'friends-list',
            builder: (context, state) {
              return Consumer<ApplicationState>(
                builder: (context, appState, _) => FriendsListPage(
                  key: ValueKey(appState.loggedIn),
                  appState: appState,
                ),
              );
            }),
        GoRoute(
            path: 'game-detail',
            builder: (context, state) {
              return Consumer<ApplicationState>(
                builder: (context, appState, _) => GameDetailPage(
                  appState: appState,
                  gameId: state.extra as String,
                ),
              );
            }),
        GoRoute(
            path: 'character-detail',
            builder: (context, state) {
              return Consumer<ApplicationState>(
                builder: (context, appState, _) => Scaffold(
                  appBar: AppBar(title: const Text("Character Detail")),
                  body: DisplayCharacterInfoWidget(
                    state.extra as Character,
                  ),
                ),
              );
            }),
        GoRoute(
            path: 'monster-detail',
            builder: (context, state) {
              return Consumer<ApplicationState>(
                builder: (context, appState, _) => MonsterDetailsPage(
                  monster: state.extra as Monster,
                ),
              );
            }),
        GoRoute(
            path: 'create-new-character',
            builder: (context, state) {
              return Consumer2<ApplicationState, NfcModel>(
                builder: (context, appState, nfcModel, _) => NewCharacterPage(
                  appState: appState,
                ),
              );
            }),
      ],
    ),
  ],
);

class App extends StatelessWidget {
  const App({super.key});

  @override
  Widget build(BuildContext context) {
    // Outlined Button Style
    final ButtonStyle outlineButtonStyle = OutlinedButton.styleFrom(
      foregroundColor: Colors.black87,
      backgroundColor: Colors.red,
      minimumSize: const Size(88, 36),
      padding: const EdgeInsets.symmetric(horizontal: 16),
      shape: const RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(10)),
      ),
    ).copyWith(
      side: MaterialStateProperty.resolveWith<BorderSide>(
        (Set<MaterialState> states) {
          if (states.contains(MaterialState.pressed)) {
            return BorderSide(
              color: Theme.of(context).colorScheme.primary,
              width: 1,
            );
          }
          return const BorderSide(); // Defer to the widget's default.
        },
      ),
    );
    return MaterialApp.router(
      title: 'Beasts and Bards',
      theme: ThemeData(
        outlinedButtonTheme: OutlinedButtonThemeData(style: outlineButtonStyle),
        fontFamily: 'Iceberg',
        // textTheme: const TextTheme(
        //     displayLarge:
        //         TextStyle(fontSize: 72.0, fontWeight: FontWeight.bold),
        //     titleLarge: TextStyle(fontSize: 36.0, fontStyle: FontStyle.italic),
        //     labelLarge: TextStyle(
        //         fontSize: 36.0,
        //         fontStyle: FontStyle.italic,
        //         color: Colors.black),
        //     bodyMedium: TextStyle(fontSize: 14.0, fontFamily: 'Iceberg')),
        visualDensity: VisualDensity.adaptivePlatformDensity,
        useMaterial3: true,
        colorSchemeSeed: Colors.red,
      ),
      routerConfig: _router,
    );
  }
}
