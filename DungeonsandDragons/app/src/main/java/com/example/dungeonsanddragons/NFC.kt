package com.example.dungeonsanddragons

import android.app.Activity
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NdefRecord.TNF_ABSOLUTE_URI
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.fragment.app.FragmentActivity

class NFC : FragmentActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // process message array
                val messageRecord = messages[0].records[0]
                val nfcData = messageRecord.payload.toString()
            }
        }
//        getContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.nfc_data)
        textView.text = "Scanned NFC"
        Log.w("NFC","Scanned NFC Tag")
    }
}