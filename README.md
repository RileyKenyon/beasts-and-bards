# Beasts and Bards
An application for managing Dungeon & Dragon campaigns, stats, and encounter handling.

![](https://github.com/RileyKenyon/beasts-and-bards/blob/main/media/beasts_and_bards_demo.gif)

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

### Run the backend (PocketBase)
Download the PocketBase binary for your OS from https://pocketbase.io/docs/ into `pocketbase/` and start it. Migrations in `pocketbase/pb_migrations` create the collections and access rules on first start.
```
cd pocketbase
./pocketbase serve
# create a superuser: ./pocketbase superuser upsert you@example.com <password>
```
Run the app against it (`10.0.2.2` is the host from an Android emulator):
```
cd beasts_and_bards
flutter run --dart-define=PB_URL=http://127.0.0.1:8090
```
Access rules are covered by `pocketbase/rules_test.py`: start a fresh server (`./pocketbase serve --dir /tmp/pbtest`) and run `python3 rules_test.py`.

Verification and password-reset mails go through Resend when `RESEND_API_KEY` is set (`pocketbase/pb_hooks/smtp.pb.js` applies the SMTP settings on every start). The sender (Settings > Mail in the admin UI, or `MAIL_FROM` to override on start) must be an address on a domain verified in Resend; `onboarding@resend.dev` only delivers to your Resend account email.

Links in those mails use the Application URL, which defaults to `http://localhost:8090` and does not work from a phone. Set it to the server's public address in Settings > Application, or with `APP_URL` on start.
```
RESEND_API_KEY=re_xxx MAIL_FROM=noreply@yourdomain.com APP_URL=https://pb.yourdomain.com ./pocketbase serve
```
Without the key, no mail is sent, but sign-in still works.

The key is saved in `pb_data/data.db` and in backups. To encrypt saved settings, add a 32-character secret and pass `--encryptionEnv` to every PocketBase command, including `superuser upsert`; without the flag it cannot read the settings. Keep the secret out of the repo. Settings saved earlier are encrypted the next time they are saved, and older backups stay readable in plain text.
```
PB_ENCRYPTION_KEY=<32 characters> ./pocketbase serve --encryptionEnv=PB_ENCRYPTION_KEY
```
The hook is covered by `pocketbase/hooks_test.py` (run `python3 hooks_test.py` in `pocketbase/`; it starts the binary itself).

### Monster data
The Monsters tab reads the SRD 5.2 (2024) creatures from the Open5e API (https://api.open5e.com/v2/). 

> This work includes material from the System Reference Document 5.2 ("SRD 5.2") by Wizards of the Coast LLC, available at https://www.dndbeyond.com/srd. The SRD 5.2 is licensed under the Creative Commons Attribution 4.0 International License, available at https://creativecommons.org/licenses/by/4.0/legalcode. Provided through Open5e (https://open5e.com).

### Cool website for fonts
Iceberg font obtained here:
https://fonts.google.com/

### FAQ
If you are running into issues with the emulator and have Android Studio installed, ensure the settings > tools > emulator is set to start outside the window. This will configure it to launch the emulators as a separate application from AndroidStudio and allow them to be launched externally.  
