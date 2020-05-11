package cf.wayzer

import cf.wayzer.ConfigExt.dataRoot
import cf.wayzer.ConfigExt.pluginMain
import cf.wayzer.script_agent.Config
import cf.wayzer.script_agent.ScriptAgent
import cf.wayzer.script_agent.ScriptManager
import net.mamoe.mirai.console.plugins.PluginBase
import java.io.File

class ScriptAgent4Mirai : PluginBase() {
    override fun onLoad() {
        ScriptAgent.load()
    }

    override fun onEnable() {
        Config.pluginMain = this
        Config.dataRoot = dataFolder.resolve("data").apply { mkdirs() }
        ScriptManager.loadDir(dataFolder.resolve("scripts"))
    }

    override fun onDisable() {
        ScriptManager.disableAll()
    }
}

/**
 * Test only
 */
fun main() {
    Config.pluginMain = ScriptAgent4Mirai()
    Config.dataRoot = File("")
    Config.compilerMode = false
    ScriptManager.loadDir(File("src"))
}