package com.github.msorkhpar.ai.claude.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.context.runner.ApplicationContextRunner

import org.springframework.ai.chat.client.advisor.api.Advisor

class ChatClientConfigTest {

    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(ChatClientConfig::class.java)

    @Test
    fun `should create defaultChatClient bean`() {
        // We provide a mock of ChatClient.Builder because the Bean depends on it
        val mockBuilder = mock(ChatClient.Builder::class.java)
        val mockChatClient = mock(ChatClient::class.java)

        `when`(mockBuilder.defaultAdvisors(any(Advisor::class.java))).thenReturn(mockBuilder)
        `when`(mockBuilder.build()).thenReturn(mockChatClient)

        contextRunner
            .withBean(ChatClient.Builder::class.java, { mockBuilder })
            .run { context ->
                assertThat(context).hasSingleBean(ChatClient::class.java)
                assertThat(context).hasBean("defaultChatClient")

                val chatClient = context.getBean<ChatClient>()
                assertThat(chatClient).isEqualTo(mockChatClient)
            }
    }
}
