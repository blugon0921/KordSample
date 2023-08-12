package kr.blugon.kordsample.events

import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.kordLogger
import dev.kord.core.on
import kotlinx.coroutines.flow.toList
import kr.blugon.kordsample.Loadable
import kr.blugon.kordsample.Main.bot
import kr.blugon.kordsample.Main.isReady
import kr.blugon.kordsample.Modules.log
import kr.blugon.kordsample.api.LogColor
import kr.blugon.kordsample.api.LogColor.color
import kr.blugon.kordsample.api.LogColor.inColor

class ClientReady: Loadable, Runnable {
    val name = "clientReady"

    override fun run() {
        kordLogger.log("${LogColor.CYAN.inColor("✔")} ${LogColor.BLUE.inColor(name)} 이벤트 불러오기 성공")
        bot.on<ReadyEvent> {
            kordLogger.log("")
            kordLogger.log("접속 서버(${bot.guilds.toList().size})".color(LogColor.CYAN))
            for(guild in bot.guilds.toList()) {
                kordLogger.log(guild.name.color(LogColor.BLUE))
            }
            bot.isReady = true
        }
    }
}