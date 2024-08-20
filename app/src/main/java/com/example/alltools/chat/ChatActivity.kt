package com.example.alltools.chat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alltools.R
import com.example.alltools.databinding.ActivityChat2Binding
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    private lateinit var messageList: MutableList<Message>
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var binding: ActivityChat2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChat2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList = mutableListOf()
        messageAdapter = MessageAdapter(messageList)
        binding.recyclerView.adapter = messageAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.sendButton.setOnClickListener {
            val messageText = binding.editText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(
                    UUID.randomUUID().toString(),
                    messageText,
                    "currentUserId",
                    "currentUserName",
                    System.currentTimeMillis()
                )
                messageList.add(newMessage)
                messageAdapter.notifyDataSetChanged()
                binding.editText.setText("")
                sendMessageToServer(newMessage)
            }
        }

        // Загрузить сохраненные сообщения из базы данных
        loadMessagesFromDatabase()

        // Получать новые сообщения с сервера (например, через веб-сокеты или периодический опрос)
        receiveMessagesFromServer()
    }

    private fun sendMessageToServer(message: Message) {
        // Отправить сообщение на сервер с использованием Retrofit или другой библиотеки для работы с сетью
    }

    private fun loadMessagesFromDatabase() {
        // Загрузить сохраненные сообщения из базы данных Room
    }

    private fun receiveMessagesFromServer() {
        // Получать новые сообщения с сервера (например, через веб-сокеты или периодический опрос)
    }
}