package com.example.alltools.nfc

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alltools.databinding.ActivityNfcactivityBinding

class NFCActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent
    private lateinit var intentFiltersArray: Array<IntentFilter>
    private lateinit var binding: ActivityNfcactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNfcactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (nfcAdapter == null) {
            Toast.makeText(this, "NFC не поддерживается на этом устройстве", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_IMMUTABLE
        )

        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        ndefFilter.addDataType("text/plain")
        intentFiltersArray = arrayOf(ndefFilter)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == action) {

            val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
            tag?.let {
                // Получаем ID карты
                val tagId = it.id
                val tagIdHex = tagId.joinToString(separator = "") { byte -> "%02X".format(byte) }

                // Получаем данные RFID метки
                val tagTechList = it.techList.joinToString(separator = "\n")

                val message = "Card ID (Hex): $tagIdHex\n\nTechnologies:\n$tagTechList"

                // Отображаем результат
                Toast.makeText(this, "NFC Tag Detected", Toast.LENGTH_LONG).show()
                showDialog(message)
            }
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .setCancelable(true)
            .show()
    }
}
