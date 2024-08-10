package kr.blugon.kordsample

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.core.exception.KordInitializationException
import io.github.classgraph.ClassGraph
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.blugon.kordmand.Command
import kr.blugon.kordsample.api.*
import java.io.File
import java.io.FileNotFoundException


object Main
lateinit var bot: Kord

private val _isReady = HashMap<Kord, Boolean>()
var Kord.isReady: Boolean
    get() {
        if(_isReady[this] == null) _isReady[this] = false
        return _isReady[this]!!
    }
    set(value) {
        _isReady[this] = value
    }

private val _isTest = HashMap<Kord, Boolean>()
var Kord.isTest: Boolean
    get() {
        if(_isTest[this] == null) _isTest[this] = false
        return _isTest[this]!!
    }
    set(value) {
        _isTest[this] = value
    }

suspend fun main(args: Array<String>) {
    val settingsFile = File("config.json")
    if(!settingsFile.exists()) { //config.json is not exist
        val resource = ClassLoader.getSystemClassLoader().getResource("config.json")?.readText() ?: throw FileNotFoundException("Failed to load config.json file")
        withContext(Dispatchers.IO) {
            settingsFile.createNewFile()
            settingsFile.writeText(resource)
        }
        return println("Please edit config.json")
    }
    val isTest = args.contains("-test")

    if(args.contains("-registerCommands")) return registerCommands(isTest)

    val rootPackage = Main.javaClass.`package`

    bot = try {
        Kord(
            if (isTest) Settings.TEST_TOKEN?: ThrowConfigException("testToken")
            else Settings.TOKEN
        )
    } catch (_: KordInitializationException) { return println("The token is invalid".color(LogColor.RED)) }
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