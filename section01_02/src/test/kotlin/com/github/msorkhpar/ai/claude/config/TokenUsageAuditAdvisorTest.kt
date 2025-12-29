package com.github.msorkhpar.ai.claude.config

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain
import org.springframework.ai.chat.metadata.ChatResponseMetadata
import org.springframework.ai.chat.metadata.Usage
import org.springframework.ai.chat.model.ChatResponse

class TokenUsageAuditAdvisorTest {

    private lateinit var advisor: TokenUsageAuditAdvisor
    private lateinit var listAppender: ListAppender<ILoggingEvent>
    private lateinit var logger: Logger

    @BeforeEach
    fun setup() {
        advisor = TokenUsageAuditAdvisor()

        logger = LoggerFactory.getLogger(TokenUsageAuditAdvisor::class.java) as Logger
        logger.level = Level.INFO
        listAppender = ListAppender()
        listAppender.start()
        logger.addAppender(listAppender)
    }

    @AfterEach
    fun teardown() {
        logger.detachAppender(listAppender)
    }

    @Test
    fun `getName should return class simple name`() {
        assertThat(advisor.name).isEqualTo("TokenUsageAuditAdvisor")
    }

    @Test
    fun `getOrder should return 0`() {
        assertThat(advisor.order).isEqualTo(0)
    }

    @Test
    fun `adviseCall should log token usage when present`() {
        // Given
        val mockRequest = mockk<ChatClientRequest>()
        val mockChain = mockk<CallAdvisorChain>()
        val mockClientResponse = mockk<ChatClientResponse>()
        val mockChatResponse = mockk<ChatResponse>()
        val mockMetadata = mockk<ChatResponseMetadata>()
        val mockUsage = mockk<Usage>()

        every { mockChain.nextCall(mockRequest) } returns mockClientResponse
        every { mockClientResponse.chatResponse() } returns mockChatResponse
        every { mockChatResponse.metadata } returns mockMetadata
        every { mockMetadata.usage } returns mockUsage
        every { mockUsage.toString() } returns "Usage[promptTokens=10, generationTokens=20, totalTokens=30]"

        // When
        val result = advisor.adviseCall(mockRequest, mockChain)

        // Then
        assertThat(result).isSameAs(mockClientResponse)

        val logsList = listAppender.list
        assertThat(logsList).hasSize(1)
        assertThat(logsList[0].level).isEqualTo(Level.INFO)
        assertThat(logsList[0].formattedMessage).contains("Usage[promptTokens=10, generationTokens=20, totalTokens=30]")
    }

}