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

private const val TAG = "MainApplication"

class MainActivity : AppCompatActivity() {
    // List of internal records from NFC read

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set Main view on launch and load D8 gif
        setContentView(R.layout.activity_main)
        val imageView: ImageView = findViewById(R.id.title_image)
        Glide.with(this).load(R.drawable.title).into(imageView)

    }
}