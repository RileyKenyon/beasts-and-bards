// Copyright 2022 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'package:beasts_and_bards/data/character.dart';
import 'package:beasts_and_bards/friends_list_page.dart';
import 'package:beasts_and_bards/new_character_form.dart';
import 'package:beasts_and_bards/nfc_reader.dart';
import 'package:beasts_and_bards/src/widgets.dart';
import 'package:firebase_ui_auth/firebase_ui_auth.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';

import 'app_state.dart';
import 'home_page.dart';
import 'dashboard_page.dart';
import 'create_game_form.dart';
import 'game_detail_page.dart';
import 'src/dio_widgets.dart';
import 'data/game.dart';
import 'nfc_notifier.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

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
          builder: (context, state) {
            return SignInScreen(
              actions: [
                ForgotPasswordAction(((context, email) {
                  final uri = Uri(
                    path: '/sign-in/forgot-password',
                    queryParameters: <String, String?>{
                      'email': email,
                    },
                  );
                  context.push(uri.toString());
                })),
                AuthStateChangeAction(((context, state) {
                  if (state is SignedIn || state is UserCreated) {
                    var user = (state is SignedIn)
                        ? state.user
                        : (state as UserCreated).credential.user;
                    if (user == null) {
                      return;
                    }
                    if (state is UserCreated) {
                      user.updateDisplayName(user.email!.split('@')[0]);
                    }
                    if (!user.emailVerified) {
                      user.sendEmailVerification();
                      const snackBar = SnackBar(
                          content: Text(
                              'Please check your email to verify your email address'));
                      ScaffoldMessenger.of(context).showSnackBar(snackBar);
                    }
                    context.replace('/');
                  }
                })),
              ],
            );
          },
          routes: [
            GoRoute(
              path: 'forgot-password',
              builder: (context, state) {
                final arguments = state.queryParams;
                return ForgotPasswordScreen(
                  email: arguments['email'],
                  headerMaxExtent: 200,
                );
              },
            ),
          ],
        ),
        GoRoute(
          path: 'profile',
          builder: (context, state) {
            return Consumer<ApplicationState>(
              builder: (context, appState, _) => ProfileScreen(
                key: ValueKey(appState.emailVerified),
                providers: const [],
                actions: [
                  SignedOutAction(
                    ((context) {
                      context.pushReplacement('/');
                    }),
                  ),
                ],
                children: [
                  Visibility(
                      visible: !appState.emailVerified,
                      child: OutlinedButton(
                        child: const Text('Recheck Verification State'),
                        onPressed: () {
                          appState.refreshLoggedInUser();
                        },
                      ))
                ],
              ),
            );
          },
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
