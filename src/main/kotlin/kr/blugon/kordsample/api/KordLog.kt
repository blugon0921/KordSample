package kr.blugon.kordsample.api


val logger = Logger("")
class Logger(val name: String) {
    fun log(msg: Any, printPrefix: Boolean = false) {
        if(printPrefix) println("[${name}] msg")
        else println(msg)
    }
}

class LogColor(val colorText: String) {
    
    companion object {
        val BLACK = LogColor("\u001B[30m")
        val RED = LogColor("\u001B[31m")
        val GREEN = LogColor("\u001B[32m")
        val YELLOW = LogColor("\u001B[33m")
        val BLUE = LogColor("\u001B[34m")
        val PURPLE = LogColor("\u001B[35m")
        val CYAN = LogColor("\u001B[36m")
        val WHITE = LogColor("\u001B[37m")

        val BOLD = LogColor("\u001B[1m")

        val DEFAULT = LogColor("\u001B[0m")

        fun String.color(color: LogColor): String {
            return "${color}${this}$DEFAULT"
        }
    }

    fun inColor(text: String): String {
        return "${this}${text}$DEFAULT"
    }
}