# 开发环境说明

## 后端 Gradle/JDK 版本

当前项目使用 Gradle 5.6.2，后端源码配置为 Java 8 字节码输出（`sourceCompatibility = 1.8`、`targetCompatibility = 1.8`）。Gradle 5.6.2 自身不能直接运行在 JDK 17/21/25 等过高版本上，否则会在 Groovy/Gradle 初始化阶段失败。

仓库已提供 `.mise.toml`，将本项目的 Gradle 运行时固定为 JDK 11。推荐命令：

```bash
mise trust
mise install
mise x java@11.0.2 -- ./gradlew :kernel:compileJava :bs-server:compileJava --no-daemon
```

如果不使用 mise，请先将 `JAVA_HOME` 指向 JDK 8 或 JDK 11，再运行 `./gradlew`。

> 说明：这是“运行 Gradle 的 JDK”限制，不代表后端源码不能继续编译启动；只要使用 JDK 8/11 启动 Gradle，仍会按项目配置编译为 Java 8 兼容字节码。
