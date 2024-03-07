package kr.blugon.kordsample

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.common.entity.PresenceStatus
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kr.blugon.kordsample.Main.bot
import java.io.File

object Main {
    lateinit var bot: ExtensibleBot

    val kordIsReady = HashMap<ExtensibleBot, Boolean>()
    var ExtensibleBot.isReady: Boolean
        get() {
            if(kordIsReady[this] == null) kordIsReady[this] = false
            return kordIsReady[this]!!
        }
        set(value) {
            kordIsReady[this] = value
        }
}

suspend fun main(args: Array<String>) {

    val rootPackage = Main.javaClass.`package`
    val TOKEN = if (args[0] == "test") Settings.TEST_TOKEN
                else Settings.TOKEN
    bot = ExtensibleBot(TOKEN) {
        presence {
            status = PresenceStatus.Online
            playing("kordSample | asdf")
        }

        @OptIn(PrivilegedIntent::class)
        intents {
            +Intent.Guilds
            +Intent.GuildVoiceStates
            +Intent.GuildMembers
            +Intent.GuildMessages
            +Intent.MessageContent
        }

        extensions {
            //Commands
            rootPackage.classesRunnable("commands").forEach { extension ->
                add { extension as Extension }
            }

            //Events
            rootPackage.classesRunnable("events").forEach { runnable ->
                (runnable as Runnable).run()
            }
        }
    }

    bot.start()
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

fun Package.classesRunnable(more: String = ""): ArrayList<Any> {
    val runnables = ArrayList<Any>()
    this.classes(more).forEach { clazz ->
        try {
            val instance = clazz.getDeclaredConstructor().newInstance()
            runnables.add(instance)
        } catch (e: Exception) {
            return@forEach
        }
    }
    return runnables
}

interface Loadable