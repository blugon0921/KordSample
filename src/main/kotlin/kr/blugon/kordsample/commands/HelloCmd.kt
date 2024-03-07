package kr.blugon.kordsample.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import dev.kord.rest.builder.message.embed
import kr.blugon.kordsample.Settings

class HelloCmd: Extension() {
    override val name = "hello"

    override suspend fun setup() {
        publicSlashCommand(::HelloArg) {
            name = "hello"
            description = "Hello, world!"

            action {
                respond {
                    embed {
                        title = "Hello, ${arguments.hello}!"
                        color = Settings.COLOR_NORMAL
                    }
                }
            }
        }
    }

    inner class HelloArg: Arguments() {
        val hello by string {
            name = "hello"
            description = "HELLO"
            require(true)
        }
    }
}