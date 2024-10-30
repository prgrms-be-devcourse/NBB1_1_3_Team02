package com.example.bookYourSeat.common.service

import com.example.bookYourSeat.common.entity.color.Color
import com.slack.api.Slack
import com.slack.api.model.Attachment
import com.slack.api.model.Field
import com.slack.api.webhook.WebhookPayloads.payload
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SlackServiceImpl : SlackService {

    @Value("\${webhook.slack.url}")
    private lateinit var slackUrl: String

    private val slackClient = Slack.getInstance()

    override fun setMessage(title: String, data: LinkedHashMap<String, String>, color: Color) {
        try {
            slackClient.send(slackUrl, payload { p ->
                p.text(title)
                    .attachments(listOf(
                        Attachment.builder().color(color.code)
                            .fields(
                                data.keys.map { key -> generateSlackField(key, data[key]!!) }
                            ).build()
                    ))
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun generateSlackField(key: String, data: String): Field {
        return Field.builder()
            .title(key)  // 필드 제목
            .value(data)  // 필드 내용
            .valueShortEnough(false)  // Slack 메시지 필드에 Slack의 메시지가 짧은지 여부
            .build()
    }
}
