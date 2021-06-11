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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.NfcBinding
import androidx.fragment.app.FragmentActivity


// Helper functions and variables
private const val TAG = "NFC"
fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

class NFC : Fragment(){
    // Initialization and binding
    private var _binding: NfcBinding? = null
    private lateinit var textView: TextView
    private val binding get() = _binding!!

    // List of internal records from NFC read
    private lateinit var currentNdefMessages: List<NdefMessage>
    private lateinit var currentNdefRecords: List<NdefRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // set the content view as the NFC layout
//        setContentView(binding.root)
        // Check if the NFC adapter triggered the application launch
//        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
//            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
//                currentNdefMessages = rawMessages.map { it as NdefMessage }
//                currentNdefRecords = currentNdefMessages.flatMap { it.records.asList() }
//
//                // Read the payload and convert to a UTF8 string
//                val textView: TextView = findViewById(R.id.nfc_data)
//                textView.text = getCurrentRecordText()
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NfcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textView = binding.nfcData
    }

    override fun onDestroyView(){
        super.onDestroyView()
        _binding = null
    }

    private fun getCurrentRecordText() : String {
        // Check NFC message type TNF (3 bit Type Name Field)
        return when (currentNdefRecords[0].tnf) {
            TNF_MIME_MEDIA -> currentNdefRecords[0].payload.toString(Charsets.UTF_8)
            TNF_WELL_KNOWN -> currentNdefRecords[0].payload.toString(Charsets.UTF_8)
            else -> ""
        }
    }

    private fun getCurrentRecordId() : String {
        // Return most recent NFC records
        // Log.d(TAG, currentNdefMessages[0].toByteArray().toHexString())
        // Log.d(TAG, currentNdefRecords[0].toMimeType())
        return currentNdefRecords[0].id.toString(Charsets.UTF_8)
    }
}