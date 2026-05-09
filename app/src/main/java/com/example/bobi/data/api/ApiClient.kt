package com.example.bobi.data.api

import com.example.bobi.data.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Request.Builder
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

object ApiClient {
    private var baseUrl: String = "http://10.0.2.2:3721/" // Android 模拟器默认指向宿主机

    fun setBaseUrl(url: String) {
        baseUrl = url.trimEnd('/') + "/"
    }

    fun getBaseUrl(): String = baseUrl

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // ===== 发送消息 =====
    suspend fun sendMessage(fromId: String, content: String, channel: String = "android"): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val json = JSONObject().apply {
                    put("from_id", fromId)
                    put("content", content)
                    put("channel", channel)
                }
                val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
                val request = Builder()
                    .url(baseUrl + "message")
                    .post(body)
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Result.success(response.body?.string() ?: "ok")
                } else {
                    Result.failure(Exception("发送失败: ${response.code}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("网络错误: ${e.localizedMessage}"))
            }
        }
    }

    // ===== 获取状态 =====
    suspend fun getStatus(): Result<SystemStatus> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Builder().url(baseUrl + "status").get().build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = JSONObject(response.body!!.string())
                    val status = SystemStatus(
                        running = json.optBoolean("running", false),
                        memoryCount = json.optInt("memory_count", 0),
                        conversationCount = json.optInt("conversation_count", 0),
                        activeTasks = json.optInt("active_tasks", 0),
                        currentTick = json.optInt("current_tick", 0),
                        provider = json.optString("provider", ""),
                        model = json.optString("model", "")
                    )
                    Result.success(status)
                } else {
                    Result.failure(Exception("获取状态失败: ${response.code}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("网络错误: ${e.localizedMessage}"))
            }
        }
    }

    // ===== 获取记忆列表 =====
    suspend fun getMemories(limit: Int = 50, search: String? = null): Result<List<Memory>> {
        return withContext(Dispatchers.IO) {
            try {
                val url = buildString {
                    append(baseUrl + "memories?limit=$limit")
                    if (!search.isNullOrBlank()) {
                        append("&search=${java.net.URLEncoder.encode(search, "UTF-8")}")
                    }
                }
                val request = Builder().url(url).get().build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val arr = JSONArray(response.body!!.string())
                    val memories = (0 until arr.length()).map { i ->
                        val obj = arr.getJSONObject(i)
                        Memory(
                            memId = obj.optString("mem_id", ""),
                            category = obj.optString("category", ""),
                            title = obj.optString("title", ""),
                            content = obj.optString("content", ""),
                            confidence = obj.optDouble("confidence", 0.0),
                            createdAt = obj.optString("created_at", "")
                        )
                    }
                    Result.success(memories)
                } else {
                    Result.failure(Exception("获取记忆失败: ${response.code}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("网络错误: ${e.localizedMessage}"))
            }
        }
    }

    // ===== 获取对话列表 =====
    suspend fun getConversations(limit: Int = 60): Result<List<Conversation>> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Builder().url(baseUrl + "conversations?limit=$limit").get().build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val arr = JSONArray(response.body!!.string())
                    val conversations = (0 until arr.length()).map { i ->
                        val obj = arr.getJSONObject(i)
                        Conversation(
                            id = obj.optString("id", ""),
                            fromId = obj.optString("from_id", ""),
                            role = obj.optString("role", ""),
                            content = obj.optString("content", ""),
                            createdAt = obj.optString("created_at", "")
                        )
                    }
                    Result.success(conversations)
                } else {
                    Result.failure(Exception("获取对话失败: ${response.code}"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("网络错误: ${e.localizedMessage}"))
            }
        }
    }

    // ===== SSE 事件流 =====
    fun eventStream(): Flow<StreamEvent> = flow {
        try {
            val request = Builder().url(baseUrl + "events").get().build()
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val reader = BufferedReader(InputStreamReader(response.body!!.byteStream()))
                reader.useLines { lines ->
                    lines.forEach { line ->
                        if (line.startsWith("data: ")) {
                            val data = line.removePrefix("data: ")
                            try {
                                val json = JSONObject(data)
                                val event = StreamEvent(
                                    type = json.optString("type", ""),
                                    content = json.optString("content", json.toString()),
                                    tick = json.optInt("tick", 0),
                                    timestamp = System.currentTimeMillis()
                                )
                                emit(event)
                            } catch (_: Exception) {
                                // 非 JSON 的 SSE 数据，作为原始事件
                                emit(StreamEvent(content = data))
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
            // 连接中断，流结束
        }
    }

    // ===== 配额信息 =====
    suspend fun getQuota(): Result<QuotaInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val request = Builder().url(baseUrl + "quota").get().build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = JSONObject(response.body!!.string())
                    Result.success(QuotaInfo(
                        used = json.optInt("used", 0),
                        limit = json.optInt("limit", 0),
                        remaining = json.optInt("remaining", 0)
                    ))
                } else {
                    Result.failure(Exception("获取配额失败"))
                }
            } catch (e: Exception) {
                Result.failure(Exception("网络错误: ${e.localizedMessage}"))
            }
        }
    }
}
