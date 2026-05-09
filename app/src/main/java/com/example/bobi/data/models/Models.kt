package com.example.bobi.data.models

// ===== 消息模型 =====
data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val fromId: String,
    val content: String,
    val channel: String = "android",
    val timestamp: Long = System.currentTimeMillis()
)

// ===== 系统状态 =====
data class SystemStatus(
    val running: Boolean = false,
    val memoryCount: Int = 0,
    val conversationCount: Int = 0,
    val activeTasks: Int = 0,
    val currentTick: Int = 0,
    val provider: String = "",
    val model: String = ""
)

// ===== 记忆 =====
data class Memory(
    val memId: String = "",
    val category: String = "",
    val title: String = "",
    val content: String = "",
    val confidence: Double = 0.0,
    val createdAt: String = ""
)

// ===== 对话 =====
data class Conversation(
    val id: String = "",
    val fromId: String = "",
    val role: String = "",
    val content: String = "",
    val createdAt: String = ""
)

// ===== SSE 事件 =====
data class StreamEvent(
    val type: String = "",
    val content: String = "",
    val tick: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

// ===== 配额信息 =====
data class QuotaInfo(
    val used: Int = 0,
    val limit: Int = 0,
    val remaining: Int = 0
)
