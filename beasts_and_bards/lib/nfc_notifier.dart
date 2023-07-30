import 'package:flutter/material.dart';
import 'package:nfc_manager/nfc_manager.dart';
import 'package:provider/provider.dart';
import 'ndef_record.dart';
import 'record.dart';

class NfcModel extends ChangeNotifier {
  bool isInitialized = false;
  bool isAvailable = false;
  NfcTag? tag;

  Future<void> startSession(Future<void> Function(NfcTag) callback) async {
    isAvailable = await NfcManager.instance.isAvailable();
    if (isAvailable) {
      NfcManager.instance.startSession(onDiscovered: (NfcTag tag) async {
        this.tag = tag;
        callback(tag);
        notifyListeners();
      });
    }
    notifyListeners();
    return;
  }

  @override
  void dispose() {
    if (isInitialized) {
      NfcManager.instance.stopSession();
    }
    super.dispose();
  }
}

class NdefWriteModel with ChangeNotifier {
  String? payload;
  void setPayload(String payload) {
    this.payload = payload;
    notifyListeners();
    return;
  }
}

class NfcReader extends StatefulWidget {
  const NfcReader({super.key});
  @override
  State<NfcReader> createState() => _NfcReader();
}

class _NfcReader extends State<NfcReader> {
  ValueNotifier<NdefMessage?> ndefMessage = ValueNotifier(null);

  Future<void> readTag(NfcTag tag) async {
    ndefMessage.value = await Ndef.from(tag)?.read();
  }

  String printPayload() {
    String payload = "empty";
    final records = ndefMessage.value?.records;
    if (records != null && records.isNotEmpty) {
      final info = NdefRecordInfo.fromNdef(records.first);
      if (info.record is WellknownTextRecord) {
        final r = info.record as WellknownTextRecord;
        payload = r.text;
      } else {
        payload = String.fromCharCodes(info.record.toNdef().payload);
      }
    }
    return payload;
  }

  @override
  Widget build(BuildContext context) {
    final nfcSession = Provider.of<NfcModel>(context);
    return FutureBuilder<void>(
      builder: (context, snapshot) {
        return Text(
            nfcSession.isAvailable ? printPayload() : "NFC Unavailable");
      },
      future: nfcSession.startSession(readTag),
    );
  }
}

class NfcWriter extends StatefulWidget {
  const NfcWriter({super.key});
  @override
  State<NfcWriter> createState() => _NfcWriter();
}

class _NfcWriter extends State<NfcWriter> {
  ValueNotifier<bool> msgWritten = ValueNotifier(false);
  String? payload;

  Future<void> writeTag(NfcTag tag) async {
    final ndef = Ndef.from(tag);
    if (ndef != null && ndef.isWritable && payload != null) {
      final record = WellknownTextRecord(languageCode: "en", text: payload!);
      NdefMessage ndefMessage = NdefMessage([record.toNdef()]);
      ndef.write(ndefMessage);
      msgWritten.value = true;
    }
  }

  @override
  Widget build(BuildContext context) {
    final ndef = Provider.of<NdefWriteModel>(context);
    payload = ndef.payload;
    ndef.addListener(() {
      payload = ndef.payload;
    });
    Provider.of<NfcModel>(context).startSession(writeTag);
    return ValueListenableBuilder<void>(
        valueListenable: msgWritten,
        builder: (context, value, child) {
          return Text(msgWritten.value ? "Written" : "Not Written");
        });
  }
}
