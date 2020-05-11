@file:DependsModule("coreLibrary")

import cf.wayzer.script_agent.Config
import coreMirai.lib.BotListener
import coreMirai.lib.RootCommands
import coreMirai.lib.botListeners
import coreMirai.lib.dataRoot
import kotlinx.coroutines.launch
import net.mamoe.mirai.event.ListeningStatus
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.subscribe

addDefaultImport("coreMirai.lib.*")
addLibraryByClass("net.mamoe.mirai.Bot")
addDefaultImport("net.mamoe.mirai.message.*")
addDefaultImport("net.mamoe.mirai.message.data.*")
generateHelper()

name = "Mirai适配器" //平台接口

onEnable {
    ConfigBuilder.init(Config.dataRoot.resolve("scriptsConfig.conf"))
    ICommands.rootProvider.set(RootCommands)
    SharedCoroutineScope.launch {
        onAfterContentEnable { child ->
            fun <T : BotEvent> BotListener<T>.listen() {
                subscribe(cls) {
                    listener(this)
                    if (child.enabled) ListeningStatus.LISTENING else ListeningStatus.STOPPED
                }
            }
            child.botListeners.forEach { it.listen() }
        }
    }
}