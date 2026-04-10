# Project Guide

## 项目概览

- **项目名：** test
- **包名：** `com.example.test`
- **语言：** Kotlin
- **UI 框架：** Jetpack Compose + Material 3
- **Min SDK：** 24 | **Target SDK：** 36

## 文件结构

```
AndroidProjects/
├── CLAUDE.md
├── ProjectGuide.md
├── ProjectStudyNotes.md
├── build.gradle.kts
├── settings.gradle.kts
└── app/src/main/
    ├── AndroidManifest.xml
    ├── java/com/example/test/
    │   ├── TestAPP.kt          # 主入口，Activity + 顶栏 + 底栏 + 主界面
    │   ├── CustomCard2.kt      # 带 studiers badge 的卡片组件
    │   ├── CustomEasyCard.kt   # 简化版卡片（无 badge）
    │   └── ui/theme/
    │       ├── Color.kt
    │       ├── Theme.kt
    │       └── Type.kt
    └── res/
        └── drawable/           # XML 图标资源（如 cards_star_24px.xml）
```

## 入口

- **Launcher Activity：** `TestAPP`
- 使用 `enableEdgeToEdge()` + `Scaffold` + `statusBarsPadding()`

## 全局变量

| 变量 | 文件 | 值 | 说明 |
|---|---|---|---|
| `unionVerticalPaddingValue` | TestAPP.kt 顶层 | 20.dp | 页面左右统一内边距 |

## 组件一览

### TestAPP.kt

| 组件 | 说明 |
|---|---|
| `CustomTopBar` | TopAppBar，包含 SearchBar（静态UI）+ Avatar Icon |
| `CustomBottomBar` | 底部导航栏，用 `forEachIndexed` 遍历 `BottomBarItem` 列表渲染 |
| `BottomBarItemView` | 单个底栏按钮（Icon + 文字），可复用 |
| `CusTitle` | 统一区块标题（20sp, Bold），只传 `text` |
| `MainInterfacePart` | 主内容区，LazyColumn 布局 |

### Data Classes

| 类 | 文件 | 字段 |
|---|---|---|
| `BottomBarItem` | TestAPP.kt | `icon`, `contentDescription`, `label`, `onClick` |
| `CustomCard2Data` | CustomCard2.kt | `title`, `cardCount`, `author`, `studierCount` |

> 注意：`CustomCard2Data` 命名加了 `Data` 后缀，避免与同文件的 Composable `CustomCard2` 冲突。

### CustomCard2.kt

- 参数：`title`, `cardCount`, `author`, `studierCount`（默认0，为0时不显示 badge）
- 宽度固定 240.dp
- 使用 `painterResource(R.drawable.cards_star_24px)` 加载外部 XML 图标

### CustomEasyCard.kt

- 参数：`title`, `cardCount`, `author`
- 宽度固定 200.dp（传入 modifier 可覆盖）
- 简化版，无 studiers badge

## 主界面布局（MainInterfacePart）

```
LazyColumn (verticalArrangement = spacedBy(sectionSpacing=46.dp))
├── item: CusTitle("Jump back in") + 大 Card（进度条 + 按钮）
├── item: CusTitle("Recents") + Box > Row（图标 + 文字）
├── item: CusTitle("For your next study session") + Row(horizontalScroll) [CustomCard2 x3]
└── item: CusTitle("Try out these flashcard sets") + Row(horizontalScroll) [CustomEasyCard x3]
```

### 间距规范

| 变量 | 值 | 用途 |
|---|---|---|
| `sectionSpacing` | 46.dp | LazyColumn 区块间距（spacedBy 自动应用）|
| `titleToContentSpacing` | 14.dp | 标题与下方内容之间的间距 |

## 横向滚动方案

使用 `Row + horizontalScroll` 而非 `LazyRow`，因为 `LazyRow` 嵌套在 `LazyColumn` 内会导致滚动卡顿。

```kotlin
Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) { ... }
```

## 图标使用方式

| 类型 | 用法 |
|---|---|
| 内置图标 | `imageVector = Icons.Default.xxx` |
| 外部 XML 图标（res/drawable） | `painter = painterResource(R.drawable.xxx)` |
