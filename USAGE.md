# Bobi 使用指南

## 🚀 快速开始

### 1. 启动 Bailongma 服务
首先确保 Bailongma 在你的电脑上运行（默认端口 3721）：
```bash
cd bailongma
npm start
```

### 2. 安装 Bobi
从 [GitHub Releases](https://github.com/qq00150610-cpu/bobi/releases) 下载最新 APK 安装。

### 3. 连接
- **模拟器**: 自动连接 `http://10.0.2.2:3721/`（无需修改）
- **真机**: 进入「设置」页面，将地址改为电脑 IP（如 `http://192.168.1.100:3721/`），点击「连接」

---

## 📱 页面功能介绍

### 💬 对话（Chat）
主聊天界面，与 Bailongma 交流的主要入口。

- **发送消息**: 底部输入框输入文字，点击发送按钮
- **AI 回复**: Bailongma 的回复实时推送到聊天列表
- **消息气泡**: 
  - 右侧紫色气泡 = 你的消息
  - 左侧青色气泡 = Bailongma 的回复
- **连接状态**: 顶部状态栏显示连接状态

### 🧠 脑图（Brain）
实时查看 Bailongma 的意识流和思考过程。

- **实时事件**: 通过 SSE 推送，每一条都是 Bailongma 的"思考"
- **事件类型**（彩色标签）:
  - 🟡 `perception` — 感知层输入
  - 🟢 `thought` — 思考过程
  - 🔵 `memory` — 记忆操作
  - 🟣 `decision` — 决策
  - 🟠 `action` — 执行动作
  - ⚪ `system` — 系统事件
- **自动滚动**: 新事件自动滚动到底部
- **最多保留**: 最近 100 条事件

### 📚 记忆库（Memories）
浏览 Bailongma 持久化的记忆。

- **记忆列表**: 展示所有已存储记忆
- **搜索**: 顶部搜索框输入关键词过滤
- **记忆卡片**:
  - 分类标签（article / task / identity / general）
  - 标题 + 内容预览
  - 置信度百分比
- **拉下刷新**: 重新加载记忆列表

### 📊 系统状态（Status）
监控 Bailongma 运行情况。

| 指标 | 说明 |
|------|------|
| 连接状态 | 已连接 / 未连接 |
| Provider | LLM 提供商（MiniMax / DeepSeek / OpenAI） |
| Model | 当前使用的模型 |
| 运行状态 | 意识循环运行中 / 已暂停 |
| 当前 TICK | 主循环计数 |
| 活跃任务 | 并行任务数量 |
| 记忆数 | 已存储记忆总数 |
| 对话记录 | 历史对话条数 |
| 配额使用 | LLM token 配额（已用 / 剩余 / 上限） |
| 最近对话 | 最近 20 条对话摘要 |

### ⚙️ 设置（Settings）
自定义 Bobi 行为。

- **服务器地址**: 修改 Bailongma 服务器 URL，支持局域网 IP
- **连接 / 断开**: 手动控制连接状态
- **用户标识**: 显示你的唯一用户 ID（Bailongma 用此 ID 区分消息来源）
- **关于**: 版本信息和功能简介

---

## 🔧 常见问题

### Q: 连接失败？
1. 检查 Bailongma 是否在电脑上启动（`npm start`）
2. 检查防火墙是否阻止 3721 端口
3. 真机确保与电脑在同一局域网
4. 模拟器使用默认地址 `10.0.2.2:3721`

### Q: 消息发送成功但没有回复？
- Bailongma 可能正在思考中，等待下一次 TICK
- 检查 Bailongma 的 LLM API Key 是否已配置
- 检查配额是否已用完

### Q: 脑图没有事件？
- 确保已连接（状态页显示「已连接」）
- Bailongma 需要处于运行状态
- 尝试发送一条消息触发 TICK 循环

### Q: 如何切换 LLM Provider？
- 在 Bailongma 的 Web 面板中配置（浏览器访问 `http://localhost:3721/`）
- 修改 Provider 后 Bobi 会自动同步显示

---

## 📊 版本历史

| 版本 | 日期 | 说明 |
|------|------|------|
| v1.0.0 | 2026-05-09 | 初始版本：对话、脑图、记忆库、状态、设置 |

---

## 🔗 相关链接

- [Bailongma 项目](https://github.com/qq00150610-cpu/bailongma) — 数字意识实验框架
- [Bobi Releases](https://github.com/qq00150610-cpu/bobi/releases) — APK 下载
- [Bailongma ACI 理念文档](https://github.com/qq00150610-cpu/bailongma/blob/main/ACI-%E7%90%86%E5%BF%B5%E6%96%87%E6%A1%A3.md)
