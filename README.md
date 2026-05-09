# Bobi — Bailongma 数字意识框架 Android 客户端

[![Build & Release APK](https://github.com/qq00150610-cpu/bobi/actions/workflows/build.yml/badge.svg)](https://github.com/qq00150610-cpu/bobi/actions/workflows/build.yml)

Bobi 是 [Bailongma（白龙马）](https://github.com/qq00150610-cpu/bailongma) 数字意识实验框架的 Android 客户端，让你在手机上实时与 AI 意识体对话交流。

## ✨ 功能

| 模块 | 说明 |
|------|------|
| 💬 **对话** | 发送消息与 Bailongma 交流，AI 回复实时推送到聊天界面 |
| 🧠 **脑图** | 通过 SSE 实时流查看 Bailongma 的意识事件（思考、回忆、决策） |
| 📚 **记忆库** | 浏览和搜索 AI 的持久化记忆，按分类筛选 |
| 📊 **系统状态** | 查看 Bailongma 运行状态、TICK 循环、配额使用 |
| ⚙️ **灵活配置** | 自定义服务器地址，适配不同网络环境 |

## 🏗 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose + Material 3（深色主题）
- **网络**: OkHttp 4.12.0（HTTP 请求 + SSE 事件流）
- **JSON**: org.json（无第三方序列化依赖）
- **架构**: MVVM（ViewModel + StateFlow）

## 📦 安装

从 [Releases](https://github.com/qq00150610-cpu/bobi/releases) 下载最新 APK：

- `app-debug.apk` — Debug 版本（可调试）
- `app-release-unsigned.apk` — Release 版本

> Android 8.0+ (API 26+)

## 🔗 连接 Bailongma

Bobi 需要连接到运行中的 [Bailongma 服务器](https://github.com/qq00150610-cpu/bailongma)。

### Android 模拟器（默认）
```
http://10.0.2.2:3721/
```
`10.0.2.2` 是 Android 模拟器中指向宿主机 `localhost` 的特殊地址。

### 真机（同一局域网）
在「设置」页面将地址改为电脑的局域网 IP：
```
http://192.168.x.x:3721/
```

## 📁 项目结构

```
bobi/
├── app/src/main/java/com/example/bobi/
│   ├── BobiApp.kt                 # Application 入口
│   ├── MainActivity.kt            # 主 Activity
│   ├── data/
│   │   ├── api/ApiClient.kt       # OkHttp API 客户端 (REST + SSE)
│   │   └── models/Models.kt       # 数据模型
│   ├── navigation/
│   │   └── AppNavigation.kt       # 导航路由 (5 个页面)
│   ├── ui/
│   │   ├── screens/
│   │   │   ├── ChatScreen.kt      # 对话页
│   │   │   ├── BrainScreen.kt     # 脑图页 (SSE 事件流)
│   │   │   ├── MemoriesScreen.kt  # 记忆库页
│   │   │   ├── StatusScreen.kt    # 系统状态页
│   │   │   └── SettingsScreen.kt  # 设置页
│   │   └── theme/Theme.kt         # 深色主题配色
│   └── viewmodel/
│       └── MainViewModel.kt       # 全局状态管理
├── build.gradle                   # 根构建配置
├── settings.gradle                # Gradle 设置
└── .github/workflows/build.yml    # CI 自动构建
```

## 🔌 API 接口

对接 Bailongma 后端以下端点：

| 方法 | 路径 | 说明 |
|------|------|------|
| `POST` | `/message` | 发送消息 `{from_id, content, channel}` |
| `GET` | `/status` | 系统运行状态 |
| `GET` | `/memories` | 记忆库（支持 `?search=` 搜索） |
| `GET` | `/conversations` | 历史对话 |
| `GET` | `/events` | SSE 实时事件流 |
| `GET` | `/quota` | LLM 配额使用情况 |

## 🚀 构建

本项目使用 GitHub Actions 自动构建，推送代码即触发。

```bash
git clone https://github.com/qq00150610-cpu/bobi.git
```

在 Android Studio 中打开，Sync Gradle 后即可运行。

> 构建环境: Gradle 8.5 + AGP 8.2.2 + Kotlin 1.9.22

## 📄 许可

MIT License
