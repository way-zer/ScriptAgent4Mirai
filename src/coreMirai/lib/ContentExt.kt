package coreMirai.lib

import cf.wayzer.script_agent.Config
import cf.wayzer.script_agent.IContentScript
import cf.wayzer.script_agent.util.DSLBuilder
import net.mamoe.mirai.console.plugins.PluginBase
import net.mamoe.mirai.event.MessageSubscribersBuilder
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.message.MessageEvent
import java.io.File
import kotlin.reflect.KClass

val Config.pluginMain by DSLBuilder.dataKeyWithDefault<PluginBase> { error("Provided") }
val Config.dataRoot by DSLBuilder.dataKeyWithDefault<File> { error("Provided") }

data class BotListener<E : BotEvent>(val cls: KClass<E>, val listener: suspend E.(E) -> Unit)

val IContentScript.botListeners by DSLBuilder.dataKeyWithDefault { mutableListOf<BotListener<*>>() }

inline fun <reified E : BotEvent> IContentScript.listen(noinline listener: suspend E.(E) -> Unit) {
    botListeners.add(BotListener(E::class, listener))
}

typealias SubscribersBuilder<T> = MessageSubscribersBuilder<T, Unit, Unit, Unit>

inline fun <reified T : MessageEvent> IContentScript.builder(body: SubscribersBuilder<T>.() -> Unit) {
    body(MessageSubscribersBuilder(Unit) { filter, listener ->
        listen<T> {
            val toString = this.message.contentToString()
            if (filter(this, toString))
                listener(this, toString)
        }
    })
}