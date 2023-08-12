package kr.blugon.kordsample

import dev.kord.core.Kord
import dev.kord.core.kordLogger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.blugon.kordsample.Modules.log
import kr.blugon.kordsample.api.Command
import kr.blugon.kordsample.api.LogColor
import kr.blugon.kordsample.api.LogColor.inColor

suspend fun main(args: Array<String>) {
    val bot = Kord(Settings.TEST_TOKEN)

    val rootPackage = Main.javaClass.`package`

    val commands = ArrayList<Command>()
    rootPackage.classes("commands").forEach { clazz ->
        try {
            val instance = clazz.getDeclaredConstructor().newInstance()
            commands.add((instance as Command))
        } catch (e: Exception) {
            return@forEach
        }
    }
    for(command in commands) {
        command.deploy(bot)
        kordLogger.log("${LogColor.CYAN.inColor("✔")} ${LogColor.CYAN.inColor(command.command)} 커맨드 등록 완료")
    }
}