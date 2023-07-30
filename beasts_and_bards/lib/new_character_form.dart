import 'package:beasts_and_bards/app_state.dart';
import 'package:beasts_and_bards/src/widgets.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:go_router/go_router.dart';
import 'package:material_design_icons_flutter/material_design_icons_flutter.dart';
import 'package:nfc_manager/nfc_manager.dart';
import 'package:uuid/uuid.dart';
import 'package:provider/provider.dart';
import 'nfc_notifier.dart';
import 'src/dio_widgets.dart';
import 'data/character.dart';

class NewCharacterPage extends StatefulWidget {
  const NewCharacterPage({super.key, required this.appState});
  final ApplicationState appState;

  @override
  State<NewCharacterPage> createState() => _NewCharacterPage();

  Set<Future<void>> submitCharacterToDatabase(Character c) =>
      {appState.addCharacterToDatabase(c, appState.activeGameId)};

  Set<Future<void>> addCharacterToActiveGame(Character c) =>
      {appState.addCharacterToActiveGame(c)};
}

class _NewCharacterPage extends State<NewCharacterPage> {
  final nameController = TextEditingController();
  final raceController = TextEditingController();
  final gameController = TextEditingController();
  final abilityControllers = List<TextEditingController>.generate(
      6, (int n) => TextEditingController(),
      growable: false);
  int counter = 0;

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // Only need the write model to be provided to subtree of new character
    return ChangeNotifierProvider(
      create: (context) => NdefWriteModel(),
      child: Scaffold(
        appBar: AppBar(title: const Text("CREATE A NEW CHARACTER")),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
            child: ListView(
              children: <Widget>[
                Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                  child: Text("Create a new Character!",
                      style: Theme.of(context).textTheme.titleLarge),
                ),
                Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                  child: TextFormField(
                    decoration: const InputDecoration(
                        icon: Icon(MdiIcons.head),
                        border: OutlineInputBorder(),
                        hintText: "Character Name",
                        labelText: "Character Name"),
                    controller: nameController,
                  ),
                ),
                Padding(
                  padding:
                      const EdgeInsets.symmetric(horizontal: 8, vertical: 8),
                  child: TextFormField(
                    decoration: const InputDecoration(
                        icon: Icon(MdiIcons.tree),
                        border: OutlineInputBorder(),
                        hintText: "Race Name",
                        labelText: "Race Name"),
                    controller: raceController,
                  ),
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Expanded(
                      child: Column(
                        children: [
                          AbilityWidget(
                              "Charisma",
                              const Icon(MdiIcons.starShooting),
                              abilityControllers[0]),
                          AbilityWidget(
                            "Constitution",
                            const Icon(MdiIcons.heart),
                            abilityControllers[1],
                            key: const Key("Constitution"),
                          ),
                          AbilityWidget(
                              "Dexterity",
                              const Icon(MdiIcons.runFast),
                              abilityControllers[2]),
                        ],
                      ),
                    ),
                    Expanded(
                      child: Column(
                        children: [
                          AbilityWidget(
                              "Intelligence",
                              const Icon(MdiIcons.brain),
                              abilityControllers[3]),
                          AbilityWidget(
                              "Strength",
                              const Icon(MdiIcons.armFlex),
                              abilityControllers[4]),
                          AbilityWidget("Wisdom", const Icon(MdiIcons.library),
                              abilityControllers[5]),
                        ],
                      ),
                    ),
                  ],
                ),
                Consumer<NdefWriteModel>(
                  builder: (context, value, child) {
                    return ElevatedButton(
                      onPressed: () {
                        Uuid uuid = const Uuid();
                        String id = uuid.v1();
                        Character character = Character(
                            name: nameController.text,
                            race: raceController.text,
                            abilities: Abilities(
                                charisma: int.parse(abilityControllers[0].text),
                                constitution:
                                    int.parse(abilityControllers[1].text),
                                dexterity:
                                    int.parse(abilityControllers[2].text),
                                intelligence:
                                    int.parse(abilityControllers[3].text),
                                strength: int.parse(abilityControllers[4].text),
                                wisdom: int.parse(abilityControllers[5].text)),
                            gameId: widget.appState.activeGameId,
                            uuid: id);
                        widget.submitCharacterToDatabase(character);
                        widget.addCharacterToActiveGame(character);
                        value.setPayload(id);

                        // Check if NFC is supported and not written to
                        if (Provider.of<NfcModel>(context, listen: false)
                            .isAvailable) {
                          showDialog(
                            context: context,
                            builder: (context) {
                              return AlertDialog(
                                title: const Text("Pair with NFC token"),
                                content: const Icon(
                                  Icons.nfc,
                                  size: 100,
                                ),
                                actions: [
                                  TextButton(
                                      onPressed: () {
                                        Navigator.of(context)
                                            .pop(); // Back to character creator
                                        Navigator.of(context)
                                            .pop(); // Back to game detail
                                      },
                                      child: const Text("Okay")),
                                ],
                              );
                            },
                          );
                        } else {
                          context.pop();
                        }
                      },
                      child: const Text("Submit Character"),
                    );
                  },
                ),
                const NfcWriter(),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
