package com.github.msorkhpar.ai.claude.configuration

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatClientConfig {

    @Bean
    fun defaultChatClient(builder: ChatClient.Builder): ChatClient {
        return builder
            .defaultAdvisors(SimpleLoggerAdvisor())
            .build()
    }
}