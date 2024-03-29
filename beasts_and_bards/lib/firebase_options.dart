// File generated by FlutterFire CLI.
// ignore_for_file: lines_longer_than_80_chars, avoid_classes_with_only_static_members
import 'package:firebase_core/firebase_core.dart' show FirebaseOptions;
import 'package:flutter/foundation.dart'
    show defaultTargetPlatform, kIsWeb, TargetPlatform;

/// Default [FirebaseOptions] for use with your Firebase apps.
///
/// Example:
/// ```dart
/// import 'firebase_options.dart';
/// // ...
/// await Firebase.initializeApp(
///   options: DefaultFirebaseOptions.currentPlatform,
/// );
/// ```
class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    if (kIsWeb) {
      return web;
    }
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return android;
      case TargetPlatform.iOS:
        return ios;
      case TargetPlatform.macOS:
        return macos;
      case TargetPlatform.windows:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for windows - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      case TargetPlatform.linux:
        throw UnsupportedError(
          'DefaultFirebaseOptions have not been configured for linux - '
          'you can reconfigure this by running the FlutterFire CLI again.',
        );
      default:
        throw UnsupportedError(
          'DefaultFirebaseOptions are not supported for this platform.',
        );
    }
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyBXTFxvZgm8ekOMw8p1GYOxvAIxHWctNfo',
    appId: '1:604995607903:web:a624f100bda8f5fdfe2aea',
    messagingSenderId: '604995607903',
    projectId: 'dndweb',
    authDomain: 'dndweb-f22ee.firebaseapp.com',
    databaseURL: 'https://dndweb-default-rtdb.firebaseio.com',
    storageBucket: 'dndweb.appspot.com',
    measurementId: 'G-DJ1JBC1BWT',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyCk3WXn8ozkHKF4oi1OdjrLwv5cjAbisUU',
    appId: '1:604995607903:android:628907a023980357fe2aea',
    messagingSenderId: '604995607903',
    projectId: 'dndweb',
    databaseURL: 'https://dndweb-default-rtdb.firebaseio.com',
    storageBucket: 'dndweb.appspot.com',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyAPy4Nij0fN7f9A5bkL0-qNP78y4ZZgP4k',
    appId: '1:604995607903:ios:8b0edc3e5d02be37fe2aea',
    messagingSenderId: '604995607903',
    projectId: 'dndweb',
    databaseURL: 'https://dndweb-default-rtdb.firebaseio.com',
    storageBucket: 'dndweb.appspot.com',
    androidClientId:
        '604995607903-c4bjp0o5ta8tnvipou566il0b4m45gi5.apps.googleusercontent.com',
    iosClientId:
        '604995607903-ls657qr8e0eiojg7ojb73ob690rnmvfs.apps.googleusercontent.com',
    iosBundleId: 'com.example.beastsAndBards',
  );

  static const FirebaseOptions macos = FirebaseOptions(
    apiKey: 'AIzaSyAPy4Nij0fN7f9A5bkL0-qNP78y4ZZgP4k',
    appId: '1:604995607903:ios:8b0edc3e5d02be37fe2aea',
    messagingSenderId: '604995607903',
    projectId: 'dndweb',
    databaseURL: 'https://dndweb-default-rtdb.firebaseio.com',
    storageBucket: 'dndweb.appspot.com',
    androidClientId:
        '604995607903-c4bjp0o5ta8tnvipou566il0b4m45gi5.apps.googleusercontent.com',
    iosClientId:
        '604995607903-ls657qr8e0eiojg7ojb73ob690rnmvfs.apps.googleusercontent.com',
    iosBundleId: 'com.example.beastsAndBards',
  );
}
