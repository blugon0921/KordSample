package kr.blugon.kordsample.events

import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on
import kotlinx.coroutines.flow.toList
import kr.blugon.kordsample.api.Event
import kr.blugon.kordsample.api.LogColor
import kr.blugon.kordsample.api.color
import kr.blugon.kordsample.api.logger
import kr.blugon.kordsample.bot
import kr.blugon.kordsample.isReady

class ClientReady: Event {
    override val name = "clientReady"

    override suspend fun register() {
        bot.on<ReadyEvent> {
            logger.log("")
            logger.log("접속 서버(${bot.guilds.toList().size})".color(LogColor.CYAN))
            for(guild in bot.guilds.toList()) {
                logger.log(guild.name.color(LogColor.BLUE))
            }
            bot.isReady = true
        }
    }
}