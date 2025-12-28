package com.github.msorkhpar.ai.claude.configuration

import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.ai.anthropic.AnthropicChatModel
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class ChatClientConfigTest {

    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(ChatClientConfig::class.java)
        .withBean(AnthropicChatModel::class.java, { mockk(relaxed = true) })
        .withBean(OllamaChatModel::class.java, { mockk(relaxed = true) })
        .withPropertyValues(
            "ollama.secondary.base-url=http://localhost:11434",
            "ollama.secondary.model=llama3"
        )

    @Test
    fun `should create default ChatClientAdvisors bean with SimpleLoggerAdvisor`() {
        contextRunner.run { context ->
            assertThat(context).hasSingleBean(ChatClientAdvisors::class.java)

            val advisors = context.getBean<ChatClientAdvisors>().advisors
            assertThat(advisors).hasSize(1)
            assertThat(advisors.first()).isInstanceOf(SimpleLoggerAdvisor::class.java)
        }
    }

    @Test
    fun `should not create default ChatClientAdvisors when custom one is provided`() {
        val customAdvisors = ChatClientAdvisors(emptyList())

        contextRunner
            .withBean(ChatClientAdvisors::class.java, { customAdvisors })
            .run { context ->
                assertThat(context).hasSingleBean(ChatClientAdvisors::class.java)
                assertThat(context.getBean<ChatClientAdvisors>()).isSameAs(customAdvisors)
            }
    }

    @ParameterizedTest(name = "Client {0} should be present={2} when enabled={2}")
    @CsvSource(
        "claude, claudeChatClient, true",
        "claude, claudeChatClient, false",
        "ollama, ollamaChatClient, true",
        "ollama, ollamaChatClient, false",
        "ollama-docker, ollamaDockerChatClient, true",
        "ollama-docker, ollamaDockerChatClient, false"
    )
    fun `should create or not create chat client based on enabled property`(
        clientName: String,
        beanName: String,
        enabled: Boolean
    ) {
        contextRunner
            .withPropertyValues("chat.client.$clientName.enabled=$enabled")
            .run { context ->
                if (enabled) {
                    assertThat(context).hasBean(beanName)
                } else {
                    assertThat(context).doesNotHaveBean(beanName)
                }
            }
    }
}
