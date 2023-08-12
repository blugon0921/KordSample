package kr.blugon.kordsample.api

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Choice
import dev.kord.rest.builder.interaction.BooleanBuilder
import dev.kord.rest.builder.interaction.IntegerOptionBuilder
import dev.kord.rest.builder.interaction.NumberOptionBuilder
import dev.kord.rest.builder.interaction.StringChoiceBuilder

interface CommandOption {
    val name: String
    val description: String
    val type: OptionType
    var required: Boolean
}


data class MentionableOption( //Mentionable
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.MENTIONABLE
    override var required = false
}

data class ChannelOption( //Channel
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.CHANNEL
    override var required = false
    val channelTypes = ArrayList<ChannelType>()
}

data class UserOption( //User
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.USER
    override var required = false
}

data class RoleOption( //Role
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.ROLE
    override var required = false
}

data class AttachmentOption( //Attachment
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.ATTACHMENT
    override var required = false
}

data class NumberOption( //Number
    override var name: String,
    override var description: String,
    var minValue: Double? = null,
    var maxValue: Double? = null,
): CommandOption {
    override val type: OptionType = OptionType.NUMBER
    override var required = false
    val choices = ArrayList<Choice<Double>>()
}

data class StringOption( //String
    override var name: String,
    override var description: String,
    var minLength: Int? = null,
    var maxLength: Int? = null,
): CommandOption {
    override val type: OptionType = OptionType.STRING
    override var required = false
    val choices = ArrayList<Choice<String>>()
}

data class IntegerOption( //Integer
    override var name: String,
    override var description: String,
    var minValue: Long? = null,
    var maxValue: Long? = null,
): CommandOption {
    override val type: OptionType = OptionType.INTEGER
    override var required = false
    val choices = ArrayList<Choice<Long>>()
}

data class BooleanOption( //Boolean
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.BOOLEAN
    override var required = false
}

data class SubCommandOption( //Boolean
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.SUB_COMMAND
    override var required = false
    var options: MutableList<Any>? = null
}

data class GroupOption( //Boolean
    override var name: String,
    override var description: String,
): CommandOption {
    override val type: OptionType = OptionType.GROUP
    override var required = false
    var subCommands: MutableList<SubCommandOption>? = null
}

enum class OptionType {
    MENTIONABLE,
    CHANNEL,
    USER,
    ROLE,
    ATTACHMENT,
    NUMBER,
    STRING,
    INTEGER,
    BOOLEAN,
    SUB_COMMAND,
    GROUP,
}