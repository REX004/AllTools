package com.example.alltools.alertDialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.alltools.R
import com.example.alltools.databinding.ActivityAlertBinding
import org.w3c.dom.Text

class AlertActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlertBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            customDialog()
        }
    }

    private fun customDialog(){
        val dialog = Dialog(this, R.style.CustomAlertDialog)
        val view = LayoutInflater.from(this).inflate(R.layout.item_custom_alert, null)
        dialog.setContentView(view)
        val okButton: Button = view.findViewById(R.id.okBT)
        val description = findViewById<TextView>(R.id.descriptionTXT)
        okButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
}