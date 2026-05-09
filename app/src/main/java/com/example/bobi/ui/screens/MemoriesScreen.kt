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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bobi.data.models.Memory
import com.example.bobi.ui.theme.*
import com.example.bobi.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoriesScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val memories by viewModel.memories.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadMemories()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("记忆库", fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { q ->
                    searchQuery = q
                    viewModel.loadMemories(search = q.ifBlank { null })
                },
                placeholder = { Text("搜索记忆...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, null, tint = NeonCyan) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkCard,
                    unfocusedContainerColor = DarkCard,
                    focusedBorderColor = ElectricViolet,
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(12.dp))

            Text(
                "共 ${memories.size} 条记忆",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(memories) { mem ->
                    MemoryCard(mem)
                }
                if (memories.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Memory, null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("暂无记忆", color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MemoryCard(memory: Memory) {
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (memory.category) {
                        "article" -> NeonCyan.copy(alpha = 0.2f)
                        "task" -> Color(0xFFFFAB40).copy(alpha = 0.2f)
                        "identity" -> Color(0xFF00E676).copy(alpha = 0.2f)
                        else -> ElectricViolet.copy(alpha = 0.2f)
                    }
                ) {
                    Text(
                        memory.category.ifEmpty { "general" },
                        fontSize = 10.sp,
                        color = when (memory.category) {
                            "article" -> NeonCyan
                            "task" -> Color(0xFFFFAB40)
                            "identity" -> Color(0xFF00E676)
                            else -> SoftLavender
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(Modifier.weight(1f))
                if (memory.confidence > 0) {
                    Text(
                        "置信度: ${"%.0f".format(memory.confidence * 100)}%",
                        fontSize = 10.sp,
                        color = Color.Gray
                    )
                }
            }
            if (memory.title.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    memory.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (memory.content.isNotBlank()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    memory.content.take(200),
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
