package com.github.msorkhpar.ai.claude.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/stream")
class ChatStreamingController(
    private val claudeChatClient: ObjectProvider<ChatClient>
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ChatStreamingController::class.java)
    }

    @GetMapping("", "/")
    fun stream(@RequestParam message: String): Flux<String> {

        val chatClient = claudeChatClient.ifAvailable
            ?: run {
                logger.warn("Claude client is not available")
                throw ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "This service is currently disabled"
                )
            }

        return chatClient.prompt(message)
            .stream()
            .content()
            .doOnComplete { logger.debug("Streaming completed") }
            .doOnError { logger.error("Error during streaming: {}", it.message, it) }
            .onErrorResume { error ->
                logger.error("Recovering from streaming error: {}", error.message)
                Flux.just("Error: Unable to complete the request. ${error.message ?: "Unknown error occurred"}")
            }
    }

}
