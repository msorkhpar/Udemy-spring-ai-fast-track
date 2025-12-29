package com.github.msorkhpar.ai.claude.controller

import com.github.msorkhpar.ai.claude.extensions.getOrThrow
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/stream")
class ChatStreamingController(
    private val claudeChatClientProvider: ObjectProvider<ChatClient>
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ChatStreamingController::class.java)
    }

    @GetMapping("", "/")
    fun stream(@RequestParam message: String): Flux<String> =
        claudeChatClientProvider.getOrThrow().prompt(message)
            .stream()
            .content()
            .doOnComplete { logger.debug("Streaming completed") }
            .doOnError { logger.error("Error during streaming: {}", it.message, it) }
            .onErrorResume { error ->
                logger.error("Recovering from streaming error: {}", error.message)
                Flux.just("Error: Unable to complete the request. ${error.message ?: "Unknown error occurred"}")
            }

}
