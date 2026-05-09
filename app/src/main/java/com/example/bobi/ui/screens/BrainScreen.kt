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
import com.example.bobi.data.models.StreamEvent
import com.example.bobi.ui.theme.*
import com.example.bobi.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrainScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val events by viewModel.streamEvents.collectAsState()
    val status by viewModel.status.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("脑图监控", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 当前 TICK 信息
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("当前 TICK: #${status.currentTick}", color = NeonCyan, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Row {
                        InfoChip("记忆", status.memoryCount.toString())
                        Spacer(Modifier.width(8.dp))
                        InfoChip("对话", status.conversationCount.toString())
                        Spacer(Modifier.width(8.dp))
                        InfoChip("任务", status.activeTasks.toString())
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("实时事件流", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(events.reversed()) { event ->
                    EventCard(event)
                }
                if (events.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("等待事件...", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = ElectricViolet.copy(alpha = 0.15f)
    ) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
            Text(label, color = SoftLavender, fontSize = 12.sp)
            Spacer(Modifier.width(4.dp))
            Text(value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EventCard(event: StreamEvent) {
    val color = when (event.type) {
        "thought" -> NeonCyan
        "response" -> Color(0xFF00E676)
        "action" -> Color(0xFFFFAB40)
        "error" -> Color(0xFFFF5252)
        else -> SoftLavender
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkCard
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = color.copy(alpha = 0.2f)
                ) {
                    Text(
                        event.type.ifEmpty { "event" },
                        fontSize = 10.sp,
                        color = color,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "#${event.tick}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
            if (event.content.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    event.content.take(300),
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
