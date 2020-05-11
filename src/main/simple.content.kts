package main

val setting by config.key("这里是默认值", "这里是配置的注释")

listen<GroupMessageEvent> {
    reply("测试:$setting")
}
builder<GroupMessageEvent> {
    "Hello" reply "你好"
}