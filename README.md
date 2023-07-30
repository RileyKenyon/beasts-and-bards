# flutter-app
Test application with flutter

## Setup
Install flutter - follow [instructions for platform](https://docs.flutter.dev/get-started/install).

### CLone repository or install binary
```
# Checkout the repo
git clone https://github.com/flutter/flutter.git -b stable

# update your path to include <flutter_install>/bin - restart terminal and confirm this works
where flutter dart
```
### Install Android SDK
If you are not using android studio, the command line tools need to be downloaded. You can find the package here:
https://developer.android.com/studio#command-tools

Extract this folder to your `Android` directory, on linux this is `$HOME/Android`.

Follow the remaining instructions here:
https://developer.android.com/tools/sdkmanager

Enable emulators:
```
cd ~/Android/Sdk/cmdline-tools/latest/bin
./sdkmanager "system-images;android-27;google_apis_playstore;x86"
```

### Install Java Development Kit (JDK)
The JDK may be installed from here:
https://www.oracle.com/java/technologies/downloads/ 

You may also download via a package manager:
```
sudo apt install default-jre
```
### Configure Flutter
```
# Configure flutter (Install android studio if you don't have it)
flutter doctor -v

# Accept licenses:
flutter doctor --android-licenses

```
### Install extension for IDE
You can do this for Android Studio / VS Code - I'll be using VS Code
https://docs.flutter.dev/get-started/editor?tab=androidstudio

### Add firebase support
Follow the firebase setup for apps:
https://firebase.google.com/docs/flutter/setup?platform=android
```
dart pub global activate flutterfire_cli
```
### Cool website for fonts
Iceberg font obtained here:
https://fonts.google.com/

### FAQ
If you are running into issues with the emulator and have Android Studio installed, ensure the settings > tools > emulator is set to start outside the window. This will configure it to launch the emulators as a separate application from AndroidStudio and allow them to be launched externally.  