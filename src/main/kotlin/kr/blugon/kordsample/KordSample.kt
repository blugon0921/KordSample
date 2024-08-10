package kr.blugon.kordsample

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import io.github.classgraph.ClassGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.blugon.kordmand.Command
import kr.blugon.kordsample.api.Event
import kr.blugon.kordsample.api.LogColor
import kr.blugon.kordsample.api.Registable
import kr.blugon.kordsample.api.logger
import kr.blugon.kordsample.exception.ConfigException
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess


object Main
lateinit var bot: Kord

private val kordIsReady = HashMap<Kord, Boolean>()
var Kord.isReady: Boolean
    get() {
        if(kordIsReady[this] == null) kordIsReady[this] = false
        return kordIsReady[this]!!
    }
    set(value) {
        kordIsReady[this] = value
    }

private val kordIsTestBot = HashMap<Kord, Boolean>()
var Kord.isTest: Boolean
    get() {
        if(kordIsTestBot[this] == null) kordIsTestBot[this] = false
        return kordIsTestBot[this]!!
    }
    set(value) {
        kordIsTestBot[this] = value
    }

suspend fun main(args: Array<String>) {
    val settingsFile = File("config.json")
    if(!settingsFile.exists()) { //config.json is not exist
        val resource = ClassLoader.getSystemClassLoader().getResource("config.json")?.readText() ?: throw FileNotFoundException("Failed to load config.json file")
        withContext(Dispatchers.IO) {
            settingsFile.createNewFile()
            settingsFile.writeText(resource)
        }
        println("Please edit config.json")
        return
    }
    val isTest = args.getOrNull(0) == "test"

    if(isTest) {
        if(Settings.TEST_TOKEN == null) ThrowConfigException("testToken")
        if(Settings.TEST_GUILD_ID == null) ThrowConfigException("testGuildId")
    }
    if(args.contains("-registerCommands")) {
        registerCommands(isTest)
        return
    }

    val rootPackage = Main.javaClass.`package`

    bot = Kord(when(isTest) {
        true -> Settings.TEST_TOKEN!!
        false -> Settings.TOKEN
    })
    bot.isTest = isTest

    //Commands
    rootPackage.classesRegistable<Registable>("commands").forEach {
        if(it is Command) logger.log("${LogColor.CYAN.inColor("✔")} ${LogColor.CYAN.inColor(it.command)} 커맨드 불러오기 성공")
        it.register()
    }

    //Events
    rootPackage.classesRegistable<Event>("events").forEach {
        logger.log("${LogColor.CYAN.inColor("✔")} ${LogColor.BLUE.inColor(it.name)} 이벤트 불러오기 성공")
        it.register()
    }

    bot.login {
//        intents += Intent.Guilds
//        intents += Intent.GuildVoiceStates
//        intents += Intent.GuildMembers
//        intents += Intent.GuildMessages
//        intents += Intent.MessageContent

        presence {
            status = PresenceStatus.Online
            playing("ㅁㄴㅇㄹ")
        }
    }
}



inline fun <reified T> Package.classes(more: String = ""): List<Class<T>> {
    val classes = ArrayList<Class<T>>()
    val packageName = if(more == "") name
    else "$name.$more"

    val scanResult = ClassGraph().acceptPackages(packageName).scan()
    scanResult.allClasses.names.forEach {
        classes.add(ClassLoader.getSystemClassLoader().loadClass(it) as Class<T>)
    }
    return classes
}

inline fun <reified T> Package.classesRegistable(more: String = ""): List<T> {
    val registable = ArrayList<T>()

    var name = "${this.name}.${more}"
    if(!name.startsWith("/")) name = "/$name"
    name.replace(".", "/")

    this.classes<T>(more).forEach { clazz ->
        try {
            val instance = clazz.getDeclaredConstructor().newInstance()
            registable.add(instance as T)
        } catch (e: Exception) {
            return@forEach
        }
    }
    return registable
}