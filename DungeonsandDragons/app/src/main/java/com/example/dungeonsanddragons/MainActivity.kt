package com.example.dungeonsanddragons

import android.util.Log
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

open class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageView : ImageView = findViewById(R.id.title_image)
        Glide.with(this).load(R.drawable.title).into(imageView)
//        Log.w("NFC","Started Application")
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // process message array
                val messageRecord = messages[0].records[0]
                val nfcData = messageRecord.payload.toString(Charsets.UTF_8)
                val textView: TextView = findViewById(R.id.nfc_data)
                textView.text = nfcData
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.w("NFC","New Intent")
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
                // process message array
                val messageRecord = messages[0].records[0]
                val nfcData = messageRecord.payload.toString()
            }
        }
//        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.nfc_data)
        textView.text = "Scanned NFC"
    }
}
// https://developer.android.com/guide/topics/connectivity/nfc/nfc?authuser=2
//fun createRecord() {
//    val uriRecord = ByteArray(0).let {emptyByteArray ->
//        NdefRecord (
//            TNF_ABSOLUTE_URI,
//            "https://developer.android.com/index.html".toByteArray(Charset.forName("US-ASCII")),
//            emptyByteArray,
//            emptyByteArray
//        )
//    }
//
//}