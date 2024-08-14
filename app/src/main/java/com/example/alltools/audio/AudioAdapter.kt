package com.example.alltools.audio
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alltools.databinding.ItemAudioBinding
import java.io.File
import java.io.IOException

class AudioAdapter(private val audioList: List<File>) :
    RecyclerView.Adapter<AudioAdapter.AudioViewHolder>() {

    inner class AudioViewHolder(private val binding: ItemAudioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var mediaPlayer: MediaPlayer? = null


        fun bind(audioFileName: String) {
            binding.audioNumber.text = "Audio ${adapterPosition + 1}"
            binding.audioNumberSame.text = "Audio ${adapterPosition + 1}"

            binding.playIMG.setOnClickListener {
                if (mediaPlayer == null) {
                    startPlaying(audioFileName)
                }
            }
            binding.stopIMG.setOnClickListener {
                stopPlaying()
            }
        }

        private fun startPlaying(audioFileName: String) {
            mediaPlayer = MediaPlayer().apply {
                try {
                    setDataSource(audioFileName)
                    prepare()
                    start()
                    binding.playIMG.visibility = View.GONE
                    binding.stopIMG.visibility = View.VISIBLE
                    binding.soundProgress.visibility = View.VISIBLE
                    binding.soundProgress.max = duration

                    setOnCompletionListener {
                        stopPlaying()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            Thread {
                while (mediaPlayer?.isPlaying == true) {
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            binding.soundProgress.progress = it.currentPosition
                        }
                    }
                    try {
                        Thread.sleep(100)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }



        private fun stopPlaying() {
            mediaPlayer?.apply {
                stop()
                release()
            }
            mediaPlayer = null
            binding.playIMG.visibility = ViewGroup.VISIBLE
            binding.stopIMG.visibility = ViewGroup.GONE
            binding.soundProgress.visibility = ViewGroup.GONE
            binding.soundProgress.progress = 0
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val binding = ItemAudioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AudioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        val audioFileName = audioList[position]
        holder.bind(audioFileName.toString())
    }

    override fun getItemCount(): Int {
        return audioList.size
    }
}