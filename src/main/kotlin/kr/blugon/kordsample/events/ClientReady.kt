package kr.blugon.kordsample.events

import dev.kord.core.event.gateway.ReadyEvent
import kotlinx.coroutines.flow.toList
import kr.blugon.kordsample.Loadable
import kr.blugon.kordsample.Main.bot
import kr.blugon.kordsample.Main.isReady
import kr.blugon.kordsample.api.LogColor
import kr.blugon.kordsample.api.LogColor.Companion.color
import kr.blugon.kordsample.api.logger

class ClientReady: Loadable, Runnable {
    val name = "clientReady"

    override fun run() {
        logger.log("${LogColor.CYAN.inColor("✔")} ${LogColor.BLUE.inColor(name)} 이벤트 불러오기 성공")
        bot.on<ReadyEvent> {
            logger.log("")
            logger.log("접속 서버(${bot.kordRef.guilds.toList().size})".color(LogColor.CYAN))
            for(guild in bot.kordRef.guilds.toList()) {
                logger.log(guild.name.color(LogColor.BLUE))
            }
            bot.isReady = true
        }
    }
}