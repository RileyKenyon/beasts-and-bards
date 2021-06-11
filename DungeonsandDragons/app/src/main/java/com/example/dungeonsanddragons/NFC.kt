package com.example.dungeonsanddragons

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NdefRecord.*
import android.nfc.NfcAdapter
import android.os.Bundle
import android.provider.Settings
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.fragment.app.FragmentActivity

private const val TAG = "NFC"
fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

class NFC : AppCompatActivity(){
    // List of internal records from NFC read
    private lateinit var currentNdefMessages: List<NdefMessage>
    private lateinit var currentNdefRecords: List<NdefRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view as the NFC layout
        setContentView(R.layout.nfc)
        // Check if the NFC adapter triggered the application launch
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                currentNdefMessages = rawMessages.map { it as NdefMessage }
                currentNdefRecords = currentNdefMessages.flatMap { it.records.asList() }

                // Read the payload and convert to a UTF8 string
                val textView: TextView = findViewById(R.id.nfc_data)
                textView.text = getCurrentRecordText()
            }
        }
    }

    private fun getCurrentRecordText() : String {
        // Return most recent NFC records

        // Check NFC message type TNF (3 bit Type Name Field)
        val payload : String = when (currentNdefRecords[0].tnf) {
            TNF_MIME_MEDIA -> currentNdefRecords[0].payload.toString(Charsets.UTF_8)
            TNF_WELL_KNOWN -> currentNdefRecords[0].payload.toString(Charsets.UTF_8)
            else -> ""
        }
        Log.d(TAG, currentNdefMessages[0].toByteArray().toHexString())
        Log.d(TAG, currentNdefRecords[0].toMimeType())
        return payload
    }

    private fun getCurrentRecordId() : String {
        // Return most recent NFC records
        return currentNdefRecords[0].id.toString(Charsets.UTF_8)
    }

//    private fun getSerialId(): String {
//        currentNdefMessages[0].toByteArray().toString()
//    }
}