package com.github.msorkhpar.ai.claude.configuration

import org.springframework.ai.anthropic.AnthropicChatModel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaChatOptions
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
@EnableConfigurationProperties(ChatClientProperties::class)
class ChatClientConfig(
    private val chatClientAdvisors: ChatClientAdvisors
) {

    companion object {
        @Bean
        @ConditionalOnMissingBean
        fun chatClientAdvisorsFallback(): ChatClientAdvisors =
            ChatClientAdvisors(listOf(SimpleLoggerAdvisor()))
    }

    @Bean
    @Primary
    @Qualifier("claudeChatClient")
    @ConditionalOnProperty(
        prefix = "chat.client.claude",
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun claudeChatClient(
        anthropicChatModel: AnthropicChatModel
    ): ChatClient = createChatClient(anthropicChatModel)

    @Bean
    @Qualifier("ollamaChatClient")
    @ConditionalOnProperty(
        prefix = "chat.client.ollama",
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun ollamaChatClient(
        ollamaChatModel: OllamaChatModel
    ): ChatClient = createChatClient(ollamaChatModel)

    @Bean
    @Qualifier("ollamaDockerChatClient")
    @ConditionalOnProperty(
        prefix = "chat.client.ollama-docker",
        name = ["enabled"],
        havingValue = "true",
        matchIfMissing = true
    )
    fun ollamaDockerChatClient(
        @Value("\${ollama.secondary.base-url}") baseUrl: String,
        @Value("\${ollama.secondary.model}") model: String
    ): ChatClient {
        val ollamaApi = OllamaApi.builder().baseUrl(baseUrl).build()
        val chatModel = OllamaChatModel.builder()
            .ollamaApi(ollamaApi)
            .defaultOptions(OllamaChatOptions.builder().model(model).build())
            .build()
        return createChatClient(chatModel)
    }

    private fun createChatClient(chatModel: ChatModel): ChatClient =
        ChatClient.builder(chatModel)
            .defaultAdvisors(chatClientAdvisors.advisors)
            .build()

}