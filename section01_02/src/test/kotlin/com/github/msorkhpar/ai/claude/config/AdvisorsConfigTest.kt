package com.github.msorkhpar.ai.claude.config

import com.github.msorkhpar.ai.claude.configuration.ChatClientAdvisors
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.beans.factory.getBean
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class AdvisorsConfigTest {

    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(AdvisorsConfig::class.java)

    @Test
    fun `should create ChatClientAdvisors bean with both SimpleLoggerAdvisor and TokenUsageAuditAdvisor`() {
        contextRunner.run { context ->
            assertThat(context).hasSingleBean(ChatClientAdvisors::class.java)

            val chatClientAdvisors = context.getBean<ChatClientAdvisors>()
            assertThat(chatClientAdvisors.advisors).hasSize(2)
            assertThat(chatClientAdvisors.advisors).allMatch { it is SimpleLoggerAdvisor || it is TokenUsageAuditAdvisor }
        }
    }

}