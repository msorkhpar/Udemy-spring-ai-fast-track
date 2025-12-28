package com.github.msorkhpar.ai.claude.config

import com.github.msorkhpar.ai.claude.configuration.ChatClientAdvisors
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdvisorsConfig {

    @Bean
    fun chatClientAdvisors(): ChatClientAdvisors = ChatClientAdvisors(
        listOf(
            SimpleLoggerAdvisor(),
            TokenUsageAuditAdvisor()
        )
    )

}