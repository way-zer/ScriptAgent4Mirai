# ScriptAgent for Mirai
Mirai Kts脚本加载器插件

README待补充

## 开发脚本
1. 拷贝本仓库(或自行配置gradle,见build.gradle.kts)
2. 在IDEA导入该项目(建议导入为Project,避免干扰)
3. 同步Gradle(.metadata文件需要在服务端才能生成,使用现有的也可)
## 基本结构
- main.init.kts (模块定义脚本)
- main(模块根目录)
    - lib(模块库目录,可放置kt,被模块所有脚本所共用,与模块同一生命周期)
    - .metadata(模块元数据,供IDE和其他编译器分析编译使用,插件运行时可以生成)
    - simple.content.kts(普通脚本)

### 注意事项
1. 重载脚本后，同一个类不一定相同，注意控制生命周期  
    如需要类似操作,可以在更长的生命周期建立抽象接口存储变量

## 版权
- coreMirai模块及依赖于该模块的子模块遵守AGPL v3协议
- ScriptAgent库归WayZer所有,协议未定,可以使用,不准修改
- coreLibrary模块归WayZer所有,协议未定,只准在该项目使用,未经许可,不准用于他处(整体或部分)
- 其余脚本由对应贡献者所有,不能违背上列原则