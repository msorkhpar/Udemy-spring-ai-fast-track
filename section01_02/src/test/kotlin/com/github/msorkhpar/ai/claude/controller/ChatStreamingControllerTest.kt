package com.github.msorkhpar.ai.claude.controller

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec
import org.springframework.ai.chat.client.ChatClient.StreamResponseSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import reactor.core.publisher.Flux

@WebFluxTest(ChatStreamingController::class)
class ChatStreamingControllerTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @TestConfiguration
    class Config {
        @Bean
        @Primary
        @Qualifier("claudeChatClient")
        fun claudeChatClient(): ChatClient {
            val chatClient = mockk<ChatClient>()
            val requestSpec = mockk<ChatClientRequestSpec>()
            val streamResponseSpec = mockk<StreamResponseSpec>()

            every { chatClient.prompt(any<String>()) } returns requestSpec
            every { requestSpec.stream() } returns streamResponseSpec
            every { streamResponseSpec.content() } returns Flux.just("Hello", " ", "World")

            return chatClient
        }
    }

    @Test
    fun `should stream response when client is available`() {
        webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/api/stream")
                    .queryParam("message", "Hello")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
            .expectBody<String>()
            .isEqualTo("Hello World")
    }

    @Test
    fun `should stream response for root path`() {
        webTestClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/api/stream/")
                    .queryParam("message", "Test")
                    .build()
            }
            .exchange()
            .expectStatus().isOk
    }
}