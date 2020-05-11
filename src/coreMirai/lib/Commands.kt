package coreMirai.lib

import cf.wayzer.script_agent.Config
import coreLibrary.lib.*
import net.mamoe.mirai.console.command.CommandManager
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.command.registerCommand

class Sender(override val player: CommandSender) : ISender<CommandSender> {
    override fun sendMessage(msg: PlaceHoldString) {
        val colorHandler: (ColorApi.Color) -> String = if (player is ConsoleCommandSender) ColorApi::consoleColorHandler
        else { _: ColorApi.Color -> "" }
        player.appendMessage(ColorApi.handle(msg.toString(), colorHandler))
    }

    override fun hasPermission(node: String): Boolean {
        return true
    }
}

typealias Commands = ICommands<Sender>

object RootCommands : Commands(null, "", "root") {
    override fun addSub(name: String, command: ICommand<in Sender>, isAliases: Boolean) {
        if (isAliases) return
        Config.pluginMain.registerCommand {
            this.name = command.name
            this.description = command.description
            this.usage = command.usage
            this.alias = command.aliases
            onCommand {
                command.handle(Sender(this), it, "/" + command.name);true
            }
        }
    }

    override fun removeSub(name: String) {
        CommandManager.unregister(name)
    }

    override fun handle(sender: Sender, arg: List<String>, prefix: String) {
        CommandManager.dispatchCommandBlocking(sender.player, prefix + arg.toString())
    }
}