package com.example.alltools.chat

data class Message(
    val id: String,
    val text: String,
    val senderId: String,
    val senderName: String,
    val timestamp: Long
)