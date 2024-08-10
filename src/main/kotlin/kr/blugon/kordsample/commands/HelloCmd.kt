package kr.blugon.kordsample.commands

import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.rest.builder.message.embed
import kr.blugon.kordmand.Command
import kr.blugon.kordmand.StringOption
import kr.blugon.kordsample.Settings
import kr.blugon.kordsample.api.Registable
import kr.blugon.kordsample.bot

class HelloCmd: Command, Registable {
    override val command = "hello"
    override val description = "Hello, world!"
    override val options = arrayListOf(StringOption("hello", "HELLO").apply {
        this.required = true
    })

    override suspend fun register() {
        onRun(bot) {
            val helloOption = interaction.command.strings["hello"]!!
            interaction.respondPublic {
                embed {
                    title = "Hello, ${helloOption}!"
                    color = Settings.COLOR_NORMAL
                }
            }
        }
    }
}