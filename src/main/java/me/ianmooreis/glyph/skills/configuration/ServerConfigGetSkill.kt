package me.ianmooreis.glyph.skills.configuration

import ai.api.model.AIResponse
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.kittinunf.result.Result
import me.ianmooreis.glyph.configs.ServerConfig
import me.ianmooreis.glyph.extensions.config
import me.ianmooreis.glyph.extensions.log
import me.ianmooreis.glyph.extensions.reply
import me.ianmooreis.glyph.orchestrators.messaging.CustomEmote
import me.ianmooreis.glyph.orchestrators.skills.Skill
import me.ianmooreis.glyph.skills.hastebin.Hastebin
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.time.Instant

/**
 * The skill for getting a server configuration which will be posted to Hastebin in YAML format
 */
object ServerConfigGetSkill : Skill("skill.configuration.view", cooldownTime = 15, guildOnly = true, requiredPermissionsUser = listOf(Permission.ADMINISTRATOR)) {
    override fun onTrigger(event: MessageReceivedEvent, ai: AIResponse) {
        event.message.contentStripped
        event.channel.sendTyping().queue()
        val configYAML = toYAML(event.guild.config,
            "Glyph Configuration for ${event.guild}\n\n" +
                "This configuration is presented to you in the easy to read and edit YAML format!\n" +
                "It is very forgiving of mistakes.\n\n" +
                "If you need help knowing what all the different values mean, visit the documentation here:\n" +
                "https://glyph-discord.readthedocs.io/en/latest/configuration.html\n\n" +
                "Click the button labeled Duplicate & Edit on the side to begin, and click save when done.\n" +
                "Then copy the url and tell Glyph \"load config URL\".")
        Hastebin.postHaste(configYAML) { _, url, response, result ->
            when (result) {
                is Result.Success -> {
                    this.log.info("Posted ${event.guild} config to $url")
                    event.message.reply(EmbedBuilder()
                        .setTitle("Configuration Viewer")
                        .setDescription("Here's the current server config:\n" +
                            "$url\n" +
                            "[Documentation](https://glyph-discord.readthedocs.io/en/latest/configuration.html) - " +
                            "[Official Glyph Server](https://discord.me/glyph-discord)")
                        .setFooter("Configuration", null)
                        .setTimestamp(Instant.now())
                        .build())
                }
                is Result.Failure -> {
                    event.message.reply("${CustomEmote.XMARK} There was an error trying to post this server's config to Hastebin, please try again later!")
                    this.log.error("Hastebin has thrown a ${response.statusCode} error when trying to post config for ${event.guild}!")
                    event.jda.selfUser.log("Hastebin", "${response.statusCode} error when trying to post config for ${event.guild}!")
                }
            }
        }
    }

    private fun toYAML(config: ServerConfig, comment: String?): String {
        val formattedComment = if (comment != null) {
            "---\n# ${comment.replace("\n", "\n# ")}\n"
        } else {
            "---"
        }
        return YAMLMapper().registerKotlinModule().writeValueAsString(config)
            .replace("\"", "")
            .replace("---", formattedComment)
    }
}