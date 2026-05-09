package com.example.bobi.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bobi.data.models.Conversation
import com.example.bobi.ui.theme.*
import com.example.bobi.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val status by viewModel.status.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()
    val quota by viewModel.quota.collectAsState()
    val conversations by viewModel.conversations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadStatus()
        viewModel.loadQuota()
        viewModel.loadConversations()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("系统状态", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "返回", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkSurface,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 连接状态
            item {
                StatusCard(
                    title = "连接状态",
                    icon = if (isConnected) Icons.Default.CheckCircle else Icons.Default.Error,
                    iconColor = if (isConnected) Color(0xFF00E676) else Color(0xFFFF5252),
                    content = if (isConnected) "已连接到 Bailongma 服务器" else "未连接",
                    details = listOf(
                        "Provider" to status.provider.ifEmpty { "-" },
                        "Model" to status.model.ifEmpty { "-" }
                    )
                )
            }

            // 运行状态
            item {
                StatusCard(
                    title = "核心运行",
                    icon = if (status.running) Icons.Default.PlayArrow else Icons.Default.Stop,
                    iconColor = if (status.running) Color(0xFF00E676) else Color(0xFFFF5252),
                    content = if (status.running) "意识循环运行中" else "已暂停",
                    details = listOf(
                        "当前 TICK" to "#${status.currentTick}",
                        "活跃任务" to status.activeTasks.toString()
                    )
                )
            }

            // 数据统计
            item {
                StatusCard(
                    title = "数据统计",
                    icon = Icons.Default.Storage,
                    iconColor = NeonCyan,
                    content = "",
                    details = listOf(
                        "记忆数" to status.memoryCount.toString(),
                        "对话记录" to status.conversationCount.toString()
                    )
                )
            }

            // 配额
            item {
                StatusCard(
                    title = "配额使用",
                    icon = Icons.Default.DataUsage,
                    iconColor = Color(0xFFFFAB40),
                    content = "",
                    details = listOf(
                        "已用" to "${quota.used} tokens",
                        "剩余" to "${quota.remaining} tokens",
                        "上限" to "${quota.limit} tokens"
                    )
                )
            }

            // 最近对话
            item {
                Text(
                    "最近对话 (${conversations.size})",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (conversations.isEmpty()) {
                item {
                    Text("暂无对话记录", color = Color.Gray, fontSize = 14.sp)
                }
            } else {
                items(conversations.takeLast(20).reversed()) { conv ->
                    ConversationRow(conv)
                }
            }
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    content: String,
    details: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(10.dp))
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            if (content.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(content, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                details.forEach { (label, value) ->
                    Column {
                        Text(label, color = Color.Gray, fontSize = 11.sp)
                        Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
fun ConversationRow(conv: Conversation) {
    val dateFormat = remember { SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            conv.role.uppercase().first().toString(),
            color = if (conv.role == "assistant") NeonCyan else SoftLavender,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            modifier = Modifier.width(20.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                conv.content.take(150),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                maxLines = 3
            )
            Text(
                conv.fromId,
                color = Color.Gray.copy(alpha = 0.5f),
                fontSize = 10.sp
            )
        }
    }
}
