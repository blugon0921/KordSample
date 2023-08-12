package kr.blugon.kordsample

import dev.kord.common.entity.PresenceStatus
import dev.kord.core.Kord
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kr.blugon.kordsample.Main.bot
import kr.blugon.kordsample.api.Command
import java.io.File

object Main {
    lateinit var bot: Kord

    val kordIsReady = HashMap<Kord, Boolean>()
    var Kord.isReady: Boolean
        get() {
            if(kordIsReady[this] == null) kordIsReady[this] = false
            return kordIsReady[this]!!
        }
        set(value) {
            kordIsReady[this] = value
        }
}

suspend fun main(args: Array<String>) {
    bot = Kord(Settings.TEST_TOKEN)
//    bot = Kord(Settings.TOKEN)

    val rootPackage = Main.javaClass.`package`

    //Commands
    rootPackage.classes("commands").forEach { clazz ->
        try {
            val instance = clazz.getDeclaredConstructor().newInstance()
            (instance as Command).deploy(bot)
            (instance as Runnable).run()
        } catch (e: Exception) {
            return@forEach
        }
    }

    //Events
    rootPackage.classesRunnable("events").forEach { runnable ->
        runnable.run()
    }

    bot.login {
        intents += Intent.Guilds
        intents += Intent.GuildVoiceStates
        @OptIn(PrivilegedIntent::class)
        intents += Intent.GuildMembers
        intents += Intent.GuildMessages
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent

        presence {
            status = PresenceStatus.Offline
            playing("command | version")
        }
    }
}


fun Package.classes(more: String = ""): ArrayList<Class<*>> {
    val classes = ArrayList<Class<*>>()
    var directory: File? = null
    val packageName = if(more == "") name
    else "${name}.${more}"

    directory = try {
        val classLoader = Thread.currentThread().getContextClassLoader()?: throw ClassNotFoundException("Can't get class loader.")
        val path: String = packageName.replace('.', '/')
        val resource = classLoader.getResource(path) ?: throw ClassNotFoundException("No resource for $path")
        File(resource.file)
    } catch (_: NullPointerException) {
        throw ClassNotFoundException("$packageName ($directory) does not appear to be a valid package")
    }
    if (directory!!.exists()) {
        val files = directory.list()
        for (i in files!!.indices) {
            if (files[i].endsWith(".class")) {
                classes.add(Class.forName(("$packageName.").toString() + files[i].substring(0, files[i].length - 6)))
            }
        }
    } else {
        throw ClassNotFoundException("$packageName does not appear to be a valid package")
    }
    return classes
}

fun Package.classesRunnable(more: String = ""): ArrayList<Runnable> {
    val runnables = ArrayList<Runnable>()
    this.classes(more).forEach { clazz ->
        try {
            val instance = clazz.getDeclaredConstructor().newInstance()
            runnables.add(instance as Runnable)
        } catch (e: Exception) {
            return@forEach
        }
    }
    return runnables
}

interface Loadable