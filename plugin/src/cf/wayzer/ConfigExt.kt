package cf.wayzer

import cf.wayzer.script_agent.Config
import cf.wayzer.script_agent.util.DSLBuilder
import net.mamoe.mirai.console.plugins.PluginBase
import java.io.File

object ConfigExt {
    var Config.pluginMain by DSLBuilder.dataKeyWithDefault<PluginBase> { error("Provided") }
    var Config.dataRoot by DSLBuilder.dataKeyWithDefault<File> { error("Provided") }
}