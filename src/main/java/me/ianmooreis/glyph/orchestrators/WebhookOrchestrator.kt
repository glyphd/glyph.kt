package me.ianmooreis.glyph.orchestrators

import me.ianmooreis.glyph.extensions.config
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.MessageEmbed
import net.dv8tion.jda.core.entities.SelfUser
import net.dv8tion.jda.webhook.WebhookClient
import net.dv8tion.jda.webhook.WebhookClientBuilder
import net.dv8tion.jda.webhook.WebhookMessageBuilder
import org.slf4j.Logger
import org.slf4j.simple.SimpleLoggerFactory

object WebhookOrchestrator {
    private val log : Logger = SimpleLoggerFactory().getLogger(this.javaClass.simpleName)

    fun send(selfUser: SelfUser, webhookUrl: String, embed: MessageEmbed) {
        getWebhookClient(selfUser.name, selfUser.avatarUrl, webhookUrl) { client, base ->
            client.send(base.addEmbeds(embed).build())
        }
    }

    fun send(name: String, avatarUrl: String?, webhookUrl: String, embed: MessageEmbed) {
        getWebhookClient(name, avatarUrl, webhookUrl) { client, base ->
            client.send(base.addEmbeds(embed).build())
        }
    }

    fun send(guild: Guild, embed: MessageEmbed) {
        getWebhookClient(guild) { client, base ->
            client.send(base.addEmbeds(embed).build())
        }
    }

    private fun getWebhookClient(guild: Guild, success: (WebhookClient, WebhookMessageBuilder) -> Unit) {
        val webhookUrl = guild.config.auditing.webhook
        if (webhookUrl != null) {
            val selfUser = guild.jda.selfUser
            val client = WebhookClientBuilder(webhookUrl).build()
            val baseMessage = WebhookMessageBuilder().setUsername(selfUser.name).setAvatarUrl(selfUser.avatarUrl)
            success(client, baseMessage)
            client.close()
        }
    }

    private fun getWebhookClient(name: String, avatarUrl: String?, webhookUrl: String, success: (WebhookClient, WebhookMessageBuilder) -> Unit) {
        val client = WebhookClientBuilder(webhookUrl).build()
        val baseMessage = WebhookMessageBuilder().setUsername(name).setAvatarUrl(avatarUrl)
        success(client, baseMessage)
        client.close()
    }
}