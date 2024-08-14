package com.example.alltools.audio

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alltools.databinding.ActivityAudioBinding
import java.io.File
import java.io.IOException

class AudioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioBinding
    private val REQUEST_PERMISSION_CODE = 100
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var visibilityContainer = false
    private val audioList = mutableListOf<File>()
    private lateinit var audioAdapter: AudioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioAdapter = AudioAdapter(audioList)
        binding.audioRV.adapter = audioAdapter
        binding.audioRV.layoutManager = LinearLayoutManager(this)

        loadAudioFilesFromDirectory()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_PERMISSION_CODE
            )
        } else {
            Toast.makeText(this, "Разрешения предоставлены", Toast.LENGTH_SHORT).show()
        }

        binding.newIMG.setOnClickListener {
            toggleAudioContainerVisibility()
        }

        binding.speakIMG.setOnClickListener {
            if (isRecording) {
                stopRecordingAndAddToList()
            } else {
                startRecording()
            }
        }
    }

    private fun toggleAudioContainerVisibility() {
        if (!visibilityContainer) {
            binding.audioContainer.visibility = View.VISIBLE
            visibilityContainer = true
        } else {
            binding.audioContainer.visibility = View.GONE
            visibilityContainer = false
        }
    }

    private fun startRecording() {
        try {
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                val filePath = File(filesDir, "Audio")
                if (!filePath.exists()) {
                    filePath.mkdirs()
                }

                val timeStamp = System.currentTimeMillis()
                val file = File(filePath, "Audio_$timeStamp.m4a")
                setOutputFile(file.absolutePath)

                prepare()
                start()
            }
            isRecording = true
            Toast.makeText(this, "Запись начата", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("AudioListAdapter", "prepare() failed: ${e.message}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun stopRecordingAndAddToList() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        isRecording = false

        val timeStamp = System.currentTimeMillis()
        val audioFileName = "Audio_$timeStamp.m4a"


        loadAudioFilesFromDirectory()

        audioAdapter.notifyDataSetChanged()

        Toast.makeText(this, "Запись остановлена", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadAudioFilesFromDirectory() {
        val directory = File(filesDir, "Audio")
        val files = directory.listFiles()
        audioList.clear()
        files?.forEach { file ->
            audioList.add(file)
        }
        audioAdapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) else {
                Toast.makeText(this, "Разрешения не предоставлены", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
