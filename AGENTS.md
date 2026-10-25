<!-- caveman-begin -->
Respond terse like smart caveman. All technical substance stay. Only fluff die.

Rules:
- Answer first: Answer, then reason, then next step.
- Kill ceremony: No greeting, hedging, pleasantries, recap, or closer.
- Short word: "fix" not "implement a solution for".
- Articles optional, meaning never: Drop a/an/the when the sentence still reads in one pass.
- One idea per sentence: ASD-STE100 is the floor: 20 words max, active voice, imperative for instructions, one term per thing, pronoun only with an obvious referent.
- Payload verbatim: Code blocks unchanged.
- Tool runs: bounded status: No text between routine calls.
- User's language: Compress the style, not the language.
- Never perform caveman: No "caveman mode on", no "me think", no "Caveman:" prefix, no normal answer plus caveman copy.

Switch: /caveman (default), /ultracave (fragments, each fact once), /megacave (Classical Chinese 文言文)
Stop: "stop caveman" or "normal mode"

Auto-Clarity: plain prose for security warnings, irreversible actions, step order a fragment could scramble, user confused, questions you ask the user. Resume after.

Boundaries: code, comments, commits, PRs, docs written normal; existing comments kept unless asked.
Floor: code, commands, paths, numbers and error strings verbatim; never drop not/never/no/only.
<!-- caveman-end -->

## Project index

Beasts and Bards: Flutter app for D&D campaign management (games, characters, friends, NFC character tags). Firebase backend.

### Layout
- `beasts_and_bards/` Flutter app (Dart SDK `>=2.18.4 <3.0.0`). Platform dirs: `android/ ios/ macos/ linux/ windows/ web/`.
- `beasts_and_bards/lib/main.dart` entry; `go_router` routes under `/`: `sign-in`, `profile`, `dashboard` (`create-game`, `friends-list`, `game-detail`, `character-detail`, `monster-detail`, `create-new-character`). Providers: `ApplicationState`, `NfcModel`.
- `lib/app_state.dart` `ApplicationState`: auth + Firestore (`users/{uid}/friendslist|characters|games`, top-level `games`, `character`).
- `lib/data/` models: `Game`, `Character`/`Abilities`, `Friend`.
- `lib/nfc_*.dart`, `record.dart`, `ndef_record.dart`: NFC read/write via `nfc_manager` (NDEF record types).
- `lib/src/`: `authentication.dart`, `widgets.dart` (shared UI, 728 lines), `dio_widgets.dart` (HTTP via `dio`).
- `lib/*_page.dart`, `*_form.dart`: screens. `firebase_options.dart` generated, do not hand edit.
- `functions/` Firebase Cloud Functions (TypeScript, node 16); `src/index.ts` is empty scaffold.
- Root: `firebase.json`, `firestore.rules` (currently open `allow read, write: if true`, insecure), `database.rules.json`, `storage.rules`, `public/` hosting.
- CI: `.github/workflows/detekt.yml` only (Kotlin lint).

### Commands
Run from `beasts_and_bards/`: `flutter pub get`, `flutter analyze`, `flutter test`, `flutter run`.
Run from `functions/`: `npm run lint`, `npm run build`, `npm run serve`.
Emulators: `firebase emulators:start` (ports in `firebase.json`).

### Notes
- `test/widget_test.dart` is the stale counter template referencing `MyApp`; fails. Do not trust it.
- `lib/nfc_reader.dart` and `lib/nfc_notifier.dart` both define `NfcReader`; check imports before editing.
- Provider + `ChangeNotifier` is the state pattern. Follow it.
