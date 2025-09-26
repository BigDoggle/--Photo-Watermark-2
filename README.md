# Photo Watermark 工具

Photo Watermark 是一个简单易用的图片水印添加工具，允许用户为图片添加文字或图片水印。
用于大语言模型辅助软件工程作业的提交。

## 功能特性

- 支持批量导入图片
- 可添加文字水印和图片水印
- 提供高级水印设置选项
- 支持水印模板管理
- 可预览水印效果
- 支持导出设置

## 技术栈

- Java Swing (图形界面)
- Java SE
- Maven (项目构建)

## 系统要求

- Java 8 或更高版本
- Windows/Linux/macOS 操作系统

## 安装与运行

### 方法一：使用预编译的可执行文件
直接运行项目中的 `watermark.exe` 文件（仅限Windows系统）。

### 方法二：从源码运行
1. 克隆或下载本项目
2. 确保已安装Java开发环境
3. 使用以下命令编译和运行：
   ```bash
   mvn clean compile exec:java
   ```

## 使用说明

1. 启动应用程序
2. 导入需要添加水印的图片
3. 选择水印类型（文字水印或图片水印）
4. 设置水印参数（位置、透明度、字体等）
5. 预览水印效果
6. 导出处理后的图片

## 项目结构

```
src/
├── main/
│   ├── java/com/bigdoggy/
│   │   ├── controller/     # 控制器类
│   │   ├── model/          # 数据模型
│   │   ├── ui/             # 用户界面组件
│   │   ├── utils/          # 工具类
│   │   └── Main.java       # 主程序入口
│   └── resources/          # 资源文件
├── templates/              # 水印模板文件
└── target/                 # 编译输出目录
```

## 主要组件

- [MainWindow.java](file:///E:/BaiduNetdiskWorkspace/%E7%A0%94%E7%A9%B6%E7%94%9F/%E8%AF%BE%E7%A8%8B/%E5%A4%A7%E8%AF%AD%E8%A8%80%E6%A8%A1%E5%9E%8B%E8%BE%85%E5%8A%A9%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B/Photo%20Watermark%202/src/main/java/com/bigdoggy/ui/MainWindow.java) - 主窗口界面
- [ImageImportController.java](file:///E:/BaiduNetdiskWorkspace/%E7%A0%94%E7%A9%B6%E7%94%9F/%E8%AF%BE%E7%A8%8B/%E5%A4%A7%E8%AF%AD%E8%A8%80%E6%A8%A1%E5%9E%8B%E8%BE%85%E5%8A%A9%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B/Photo%20Watermark%202/src/main/java/com/bigdoggy/controller/ImageImportController.java) - 图片导入控制器
- [TextWatermarkDialog.java](file:///E:/BaiduNetdiskWorkspace/%E7%A0%94%E7%A9%B6/%E8%AF%BE%E7%A8%8B/%E5%A4%A7%E8%AF%AD%E8%A8%80%E6%A8%A1%E5%9E%8B%E8%BE%85%E5%8A%A9%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B/Photo%20Watermark%202/src/main/java/com/bigdoggy/ui/TextWatermarkDialog.java) - 文字水印设置对话框
- [ImageWatermarkDialog.java](file:///E:/BaiduNetdiskWorkspace/%E7%A0%94%E7%A9%B6/%E8%AF%BE%E7%A8%8B/%E5%A4%A7%E8%AF%AD%E8%A8%80%E6%A8%A1%E5%9E%8B%E8%BE%85%E5%8A%A9%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B/Photo%20Watermark%202/src/main/java/com/bigdoggy/ui/ImageWatermarkDialog.java) - 图片水印设置对话框
- [TemplateManagerDialog.java](file:///E:/BaiduNetdiskWorkspace/%E7%A0%94%E7%A9%B6/%E8%AF%BE%E7%A8%8B/%E5%A4%A7%E8%AF%AD%E8%A8%80%E6%A8%A1%E5%9E%8B%E8%BE%85%E5%8A%A9%E8%BD%AF%E4%BB%B6%E5%B7%A5%E7%A8%8B/Photo%20Watermark%202/src/main/java/com/bigdoggy/ui/TemplateManagerDialog.java) - 水印模板管理对话框

