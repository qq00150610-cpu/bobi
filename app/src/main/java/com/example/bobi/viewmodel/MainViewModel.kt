package com.example.bobi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bobi.data.api.ApiClient
import com.example.bobi.data.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    // ===== 服务器连接 =====
    private val _serverUrl = MutableStateFlow("http://10.0.2.2:3721/")
    val serverUrl: StateFlow<String> = _serverUrl.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    // ===== 系统状态 =====
    private val _status = MutableStateFlow(SystemStatus())
    val status: StateFlow<SystemStatus> = _status.asStateFlow()

    // ===== 聊天 =====
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // ===== 记忆 =====
    private val _memories = MutableStateFlow<List<Memory>>(emptyList())
    val memories: StateFlow<List<Memory>> = _memories.asStateFlow()

    // ===== 对话记录 =====
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations.asStateFlow()

    // ===== 实时事件流 =====
    private val _streamEvents = MutableStateFlow<List<StreamEvent>>(emptyList())
    val streamEvents: StateFlow<List<StreamEvent>> = _streamEvents.asStateFlow()

    // ===== 配额 =====
    private val _quota = MutableStateFlow(QuotaInfo())
    val quota: StateFlow<QuotaInfo> = _quota.asStateFlow()

    // ===== 用户ID =====
    private val _userId = MutableStateFlow("User-${(1000..9999).random()}")
    val userId: StateFlow<String> = _userId.asStateFlow()

    // ===== 输入文本 =====
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    fun updateInput(text: String) { _inputText.value = text }

    fun setServerUrl(url: String) {
        _serverUrl.value = url
        ApiClient.setBaseUrl(url)
    }

    fun connect() {
        viewModelScope.launch {
            _isConnected.value = true
            loadStatus()
            startEventStream()
        }
    }

    fun disconnect() {
        _isConnected.value = false
        _status.value = SystemStatus()
    }

    fun loadStatus() {
        viewModelScope.launch {
            val result = ApiClient.getStatus()
            result.onSuccess { _status.value = it }
            result.onFailure { _isConnected.value = false }
        }
    }

    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isEmpty()) return

        val msg = ChatMessage(fromId = _userId.value, content = text)
        _messages.value = _messages.value + msg
        _inputText.value = ""
        _isLoading.value = true

        viewModelScope.launch {
            val result = ApiClient.sendMessage(_userId.value, text)
            _isLoading.value = false
            if (result.isSuccess) {
                // 响应会通过 SSE 流获取
                loadStatus()
            } else {
                _messages.value = _messages.value + ChatMessage(
                    fromId = "system",
                    content = "发送失败: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun loadMemories(search: String? = null) {
        viewModelScope.launch {
            ApiClient.getMemories(search = search).onSuccess {
                _memories.value = it
            }
        }
    }

    fun loadConversations() {
        viewModelScope.launch {
            ApiClient.getConversations().onSuccess {
                _conversations.value = it
            }
        }
    }

    fun loadQuota() {
        viewModelScope.launch {
            ApiClient.getQuota().onSuccess {
                _quota.value = it
            }
        }
    }

    private fun startEventStream() {
        viewModelScope.launch {
            ApiClient.eventStream().collect { event ->
                _streamEvents.value = (_streamEvents.value + event).takeLast(100)
                // 如果事件包含 AI 回复文本，添加到聊天
                if (event.type == "thought" || event.type == "response" || event.type == "message") {
                    _messages.value = _messages.value + ChatMessage(
                        fromId = "bailongma",
                        content = event.content,
                        channel = event.type
                    )
                }
                // 每收到事件刷新状态
                if (event.tick > 0) {
                    loadStatus()
                }
            }
        }
    }
}
