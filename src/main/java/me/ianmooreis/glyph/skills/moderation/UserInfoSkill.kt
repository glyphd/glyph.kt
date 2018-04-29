package me.ianmooreis.glyph.skills.moderation

import ai.api.model.AIResponse
import me.ianmooreis.glyph.extensions.getInfoEmbed
import me.ianmooreis.glyph.extensions.reply
import me.ianmooreis.glyph.orchestrators.SkillAdapter
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent

object UserInfoSkill : SkillAdapter("skill.moderation.userInfo") { //TODO: Change to camelcase before release
    override fun onTrigger(event: MessageReceivedEvent, ai: AIResponse) {
        val userName: String? = ai.result.getStringParameter("user", null)
        val user: User? = if (event.channelType.isGuild && userName != null) {
            event.guild.getMembersByEffectiveName(userName, true).getOrNull(0)?.user ?:
            event.guild.getMembersByName(userName, true).getOrNull(0)?.user ?:
            event.guild.getMembersByNickname(userName, true).getOrNull(0)?.user
        } else {
            event.author
        }
        if (user == null) {
            event.message.reply("Unable to find the specified user!")
            return
        }
        event.message.reply(user.getInfoEmbed("User Info", "Moderation", null, true, false))
    }
}