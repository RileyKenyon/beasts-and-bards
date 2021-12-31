package com.example.dungeonsanddragons

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NdefRecord.*
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.TextView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dungeonsanddragons.databinding.FragmentNfcBinding


// Helper functions and variables
private const val TAG = "NFC"
fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

class NfcFragment : Fragment(){
    // Initialization and binding
    private var _binding: FragmentNfcBinding? = null
    private lateinit var idTextView: TextView
    private lateinit var payloadTextView: TextView

    private val binding get() = _binding!!

    // List of internal records from NFC read
    private lateinit var currentNdefMessages: List<NdefMessage>
    private lateinit var currentNdefRecords: List<NdefRecord>
    private var id : String = "Dummy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Get messages and ID
        id = arguments?.getByteArray("id")!!.toHexString()
        currentNdefMessages = arguments?.getParcelableArrayList<NdefMessage>("rawMessages")!!
        currentNdefRecords = currentNdefMessages.flatMap { it.records.asList() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNfcBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        idTextView = binding.nfcId
        payloadTextView = binding.nfcData

        // Read the payload and convert to a UTF8 string
        payloadTextView.text = getCurrentRecordText()
        idTextView.text = id
        Log.i("TAG ID",idTextView.text.toString())

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