package com.example.dungeonsanddragons

import android.util.Log
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

open class MainActivity : AppCompatActivity() {
    // List of internal records from NFC read
    private lateinit var currentNdefRecords: List<NdefRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Main view on launch and load D8 gif
        setContentView(R.layout.activity_main)
        val imageView: ImageView = findViewById(R.id.title_image)
        Glide.with(this).load(R.drawable.title).into(imageView)

        // Check if the NFC adapter triggered the application launch
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                currentNdefRecords = messages.flatMap { it.records.asList() }

                // Read the payload and convert to a UTF8 string
                val textView: TextView = findViewById(R.id.nfc_data)
                textView.text = getCurrentRecordText()
            }
        }
    }

    private fun getCurrentRecordText() : String {
        // Return most recent NFC records
        return currentNdefRecords[0].payload.toString(Charsets.UTF_8)
    }
}