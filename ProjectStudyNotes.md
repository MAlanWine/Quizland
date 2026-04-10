# Compose 学习笔记

## 目录

1. [基础布局组件](#1-基础布局组件)
2. [Modifier 系统](#2-modifier-系统)
3. [对齐与排列：Alignment vs Arrangement](#3-对齐与排列alignment-vs-arrangement)
4. [间距：Spacer vs padding](#4-间距spacer-vs-padding)
5. [懒加载列表](#5-懒加载列表)
6. [Material3 组件](#6-material3-组件)
7. [颜色系统](#7-颜色系统)
8. [图标使用](#8-图标使用)

---

## 1. 基础布局组件

### Row
水平排列子元素。

```kotlin
Row(
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
) { ... }
```

### Column
垂直排列子元素。

```kotlin
Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) { ... }
```

### Box
层叠布局，子元素可以重叠。常用于给某个区域加 clickable 而不影响内部布局。

```kotlin
Box(
    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable {}
) { ... }
```

---

## 2. Modifier 系统

### 核心规则：顺序即优先级，从外到内

Modifier 链像嵌套的结构体，**先写的在外层，后写的在内层**。

```
Modifier
    .padding(外部间距)   ← 最外层
    .background(背景)
    .padding(内部间距)   ← 背景内部的空白
    .size(大小)          ← 最内层
```

类比 C 的嵌套 struct：
```c
struct {                  // background（外层）
    char padding[12];     // padding（中间）
    struct {              // size（内层）
        Icon icon;
    } inner;
} outer;
```

### 常用 Modifier

| Modifier | 说明 |
|---|---|
| `.size(dp)` | 设置宽高 |
| `.width(dp)` / `.height(dp)` | 单独设置宽或高 |
| `.fillMaxSize()` | 填满父容器宽高 |
| `.fillMaxWidth()` | 填满父容器宽度 |
| `.fillMaxHeight()` | 填满父容器高度（需父容器有明确高度）|
| `.padding(dp)` | 内/外边距（取决于在链中的位置）|
| `.background(color, shape)` | 背景色 + 形状（圆角等）|
| `.clip(shape)` | 裁剪形状（clipable 的涟漪也会被裁剪）|
| `.clickable { }` | 点击事件，自带涟漪效果 |
| `.weight(1f)` | 在 Row/Column 中占剩余空间 |
| `.horizontalScroll(state)` | 开启横向滚动 |

### padding 内外的区别

```kotlin
// padding 在 background 前 → 外部，不在背景色范围内
Modifier.padding(12.dp).background(Color.Red)

// padding 在 background 后 → 内部，在背景色范围内
Modifier.background(Color.Red).padding(12.dp)
```

### clip 和 clickable 的顺序

涟漪效果会被 `clip` 限制，所以 `clip` 必须在 `clickable` 前面：

```kotlin
Modifier
    .clip(RoundedCornerShape(8.dp))   // 先裁剪
    .clickable { }                     // 再加点击，涟漪在圆角内
```

### fillMaxHeight 的限制

`fillMaxHeight()` 需要父容器有明确的高度才能生效。如果父容器高度是由子元素撑起的（动态高度），`fillMaxHeight()` 无法确定要填满多少，不会生效。

### weight 的用途

- 在 Row/Column 中让某个元素占满剩余空间
- 两个元素推到两端：`Spacer(Modifier.weight(1f))`

---

## 3. 对齐与排列：Alignment vs Arrangement

| | 说明 | 类比 |
|---|---|---|
| `Alignment` | 元素在容器中的**位置**（钉到某处）| 定点坐标 |
| `Arrangement` | 元素之间的**间距分布** | 弹性分配剩余空间 |

### 在 Column 里

| 参数 | 控制方向 | 常用值 |
|---|---|---|
| `horizontalAlignment` | 水平（左中右）| `Alignment.CenterHorizontally` |
| `verticalArrangement` | 垂直（间距）| `Arrangement.Center`, `Arrangement.spacedBy(dp)` |

### 在 Row 里

| 参数 | 控制方向 | 常用值 |
|---|---|---|
| `verticalAlignment` | 垂直（上中下）| `Alignment.CenterVertically` |
| `horizontalArrangement` | 水平（间距）| `Arrangement.Center`, `Arrangement.SpaceBetween` |

### 谁有多余空间，谁来决定居中

父容器有剩余空间 → 由父容器的 Alignment/Arrangement 决定子元素位置。子元素本身没有多余空间，自身的对齐设置无意义。

例：Row 被 Icon（64dp）撑起，Column 只有 Text 高度（30dp）：
- `Column` 的 `verticalArrangement = Center` → 无效（Column 内没多余空间）
- `Row` 的 `verticalAlignment = CenterVertically` → 有效（Row 有 64-30=34dp 多余空间）

### Arrangement.spacedBy

自动在子元素之间插入固定间距，相当于自动加 Spacer：

```kotlin
Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { ... }
// 等价于每两个元素之间插入 Spacer(Modifier.size(12.dp))
```

---

## 4. 间距：Spacer vs padding

| | 归属 | 适用场景 |
|---|---|---|
| `padding` | 属于元素自身（背景色、点击区域都包含）| 元素内容离自身边缘的距离 |
| `Spacer` | 独立的空白占位块 | 两个不相关元素之间的间隔 |

```kotlin
// padding：背景色包含空白
Text(modifier = Modifier.background(Red).padding(16.dp))

// Spacer：背景色不包含空白
Spacer(Modifier.height(16.dp))
Text(modifier = Modifier.background(Red))
```

简单场景下两者效果相同，推荐规则：
- 元素内容离边缘 → `padding`
- 两个元素之间 → `Spacer` 或 `Arrangement.spacedBy`

---

## 5. 懒加载列表

### LazyColumn / LazyRow

懒加载：只渲染屏幕内可见的元素，数据量大时性能好。

```kotlin
LazyColumn(
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    item { /* 单个元素 */ }
    items(10) { index -> /* 批量元素 */ }
}
```

### forEachIndexed

```kotlin
items.forEachIndexed { index, item ->
    // index: 当前下标（Int，从0开始）
    // item: 当前元素值
    // index 和 item 的名字是自己起的，不固定
}
```

### 嵌套懒加载的冲突问题

`LazyColumn` 内嵌套 `LazyRow` 会卡顿，原因是两者都需要动态测量尺寸，形成循环依赖。

**规则：嵌套滚动中，同一方向最多一个 Lazy。**

| 外层 | 内层 | 结果 |
|---|---|---|
| `LazyColumn` | `Row + horizontalScroll` | ✓ 流畅 |
| `LazyColumn` | `LazyRow` | ✗ 卡顿 |

推荐方案（数据量少时）：
```kotlin
Row(
    modifier = Modifier.horizontalScroll(rememberScrollState()),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
) { ... }
```

---

## 6. Material3 组件

### Scaffold
页面框架，自动处理顶栏/底栏/内容区的位置关系。

```kotlin
Scaffold(
    topBar = { CustomTopBar() },
    bottomBar = { BottomAppBar(...) }
) { innerPadding ->
    Content(modifier = Modifier.padding(innerPadding))
}
```

### TopAppBar

```kotlin
TopAppBar(
    title = { Text("标题") },
    navigationIcon = { /* 左侧图标，如菜单/返回 */ },
    actions = { /* 右侧按钮区域，从左到右排列 */ }
)
```

### BottomAppBar

```kotlin
BottomAppBar(
    actions = { /* 底栏内容 */ }
)
```

### SearchBar（静态 UI 写法）

```kotlin
SearchBar(
    inputField = {
        SearchBarDefaults.InputField(
            query = "",
            onQueryChange = {},
            onSearch = {},
            expanded = false,
            onExpandedChange = {},
            placeholder = { Text("Search") }
        )
    },
    expanded = false,
    onExpandedChange = {}
) {}
```

### Card

```kotlin
Card(
    shape = RoundedCornerShape(16.dp)
) { /* 内容 */ }
```

### IconButton

默认大小 48x48dp，用 `Modifier.size()` 调整：

```kotlin
IconButton(
    onClick = {},
    modifier = Modifier.size(56.dp)
) {
    Icon(
        imageVector = Icons.Default.Home,
        modifier = Modifier.size(28.dp)  // Icon 也要同步调整
    )
}
```

### Button 颜色

```kotlin
Button(
    colors = ButtonDefaults.buttonColors(
        containerColor = Color(68, 73, 227),  // 背景色
        contentColor = Color.White             // 文字/图标色
    )
) { ... }
```

### LinearProgressIndicator

```kotlin
LinearProgressIndicator(
    progress = { 0.6f },
    modifier = Modifier.fillMaxWidth().height(8.dp),  // height 控制粗细
    color = Color(226, 135, 67),
    trackColor = ProgressIndicatorDefaults.linearTrackColor
)
```

### Icon 加背景色和圆角

```kotlin
Icon(
    imageVector = Icons.Default.Home,
    tint = Color.White,  // icon 本身颜色
    modifier = Modifier
        .size(48.dp)
        .background(color = Color.Blue, shape = RoundedCornerShape(8.dp))
        .padding(10.dp)  // icon 到背景边缘的距离
)
```

圆形背景：`.background(color, shape = CircleShape)`

### Row 添加点击事件

`Row` 没有 `onClick` 参数，用 `Modifier.clickable`：

```kotlin
Row(modifier = Modifier
    .clip(RoundedCornerShape(8.dp))  // clip 在 clickable 前，涟漪跟着圆角走
    .clickable { }
) { ... }
```

---

## 7. 颜色系统

### 直接使用 Color

```kotlin
Color(0xFFFF6600)       // ARGB hex，0xFF = 不透明
Color(226, 135, 67)     // RGB，默认不透明
Color.White / Color.Red // 预设颜色
```

颜色格式 `0xAARRGGBB`：AA=透明度，RR/GG/BB=红绿蓝，与 C 的 hex 颜色一致。

### Material3 主题色

```kotlin
MaterialTheme.colorScheme.primary
MaterialTheme.colorScheme.secondary
MaterialTheme.colorScheme.background
MaterialTheme.colorScheme.surface
MaterialTheme.colorScheme.error
```

推荐在 `Color.kt` 和 `Theme.kt` 里统一定义主题色，组件内用 `colorScheme.xxx` 引用，而不是每处硬编码颜色值。

---

## 8. 图标使用

### 内置图标（Icons.Default）

```kotlin
Icon(
    imageVector = Icons.Default.Home,
    contentDescription = "Home"
)
```

需要 import：`androidx.compose.material.icons.filled.Home`

### 外部 XML 图标（res/drawable）

```kotlin
Icon(
    painter = painterResource(id = R.drawable.my_icon),
    contentDescription = "My Icon"
)
```

需要 import：`androidx.compose.ui.res.painterResource`

`imageVector` 只接受 `ImageVector` 类型（内置图标），外部资源文件必须用 `painter` 参数。

### 头像（圆形图片）

```kotlin
Image(
    painter = painterResource(id = R.drawable.avatar),
    contentDescription = "Avatar",
    contentScale = ContentScale.Crop,
    modifier = Modifier.size(40.dp).clip(CircleShape)
)
```
