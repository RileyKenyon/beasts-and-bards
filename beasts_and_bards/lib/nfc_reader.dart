/**
 * NFC Reader class for reading tokens
 */
import 'package:flutter/material.dart';
import 'package:nfc_manager/nfc_manager.dart';

// For logging
import 'dart:developer';

class NfcReader extends StatefulWidget {
  @override
  State<NfcReader> createState() => _NfcReader();
}

class _NfcReader extends State<NfcReader> {
  ValueNotifier<dynamic> result = ValueNotifier(null);

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('NfcManager Plugin Example')),
      body: SafeArea(
        child: FutureBuilder<bool>(
          future: NfcManager.instance.isAvailable(),
          builder: (context, ss) => ss.data != true
              ? Center(child: Text('NfcManager.isAvailable(): ${ss.data}'))
              : Flex(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  direction: Axis.vertical,
                  children: [
                    Flexible(
                      flex: 2,
                      child: Container(
                        margin: const EdgeInsets.all(4),
                        constraints: const BoxConstraints.expand(),
                        decoration: BoxDecoration(border: Border.all()),
                        child: SingleChildScrollView(
                          child: ValueListenableBuilder<dynamic>(
                            valueListenable: result,
                            builder: (context, value, _) =>
                                Text('${value ?? ''}'),
                          ),
                        ),
                      ),
                    ),
                    ElevatedButton(
                        onPressed: _tagRead, child: const Text('Tag Read')),
                  ],
                ),
        ),
      ),
    );
  }

  void _tagRead() {
    NfcManager.instance.startSession(onDiscovered: (NfcTag tag) async {
      result.value = tag.data;
      final ndefTag = Ndef.from(tag);
      if (ndefTag != null) {}
      NfcManager.instance.stopSession();
    });
  }
}





  // Future<void> init() async {
  //   bool isAvailable = await NfcManager.instance.isAvailable();
  //   NfcManager.instance.startSession(
  //     onDiscovered: (NfcTag tag) async {},
  //   );
  // }


//   void exit() {
//     // Stop Session
//     NfcManager.instance.stopSession();
//   }
// }
  // }