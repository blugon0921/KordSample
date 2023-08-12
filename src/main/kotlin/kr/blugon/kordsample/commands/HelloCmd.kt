package kr.blugon.kordsample.commands

import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.kordLogger
import dev.kord.core.on
import dev.kord.rest.builder.message.create.embed
import kr.blugon.kordsample.Command
import kr.blugon.kordsample.Main.bot
import kr.blugon.kordsample.Modules.log
import kr.blugon.kordsample.api.LogColor
import kr.blugon.kordsample.api.LogColor.inColor
import kr.blugon.kordsample.api.StringOption
import kr.blugon.kordsample.Settings

class HelloCmd: Command, Runnable {
    override val command = "hello"
    override val description = "Hello, world!"
    override val options = listOf(
        StringOption("option", "Option").apply {
            this.required = true
        }
    )

    override suspend fun deploy(bot: Kord) {
        super.deploy(bot)
    }

     override fun run() {
        kordLogger.log("${LogColor.CYAN.inColor("✔")} ${LogColor.CYAN.inColor(command)} 커맨드 불러오기 성공")
        bot.on<GuildChatInputCommandInteractionCreateEvent> {
            if(interaction.command.rootName != command) return@on

            interaction.respondPublic {
                embed {
                    title = "Hello, world!"
                    color = Settings.COLOR_NORMAL
                }
            }
        }
    }
}