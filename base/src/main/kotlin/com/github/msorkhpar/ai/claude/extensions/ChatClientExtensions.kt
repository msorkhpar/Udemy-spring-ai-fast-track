package com.github.msorkhpar.ai.claude.extensions

import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

private val logger = LoggerFactory.getLogger("ChatClientExtensions")

/**
 * Extension function to get ChatClient from ObjectProvider with proper error handling.
 *
 * @throws ResponseStatusException with SERVICE_UNAVAILABLE status if client is not available
 * @return ChatClient instance if available
 */
fun ObjectProvider<ChatClient>.getOrThrow(): ChatClient {
    return this.ifAvailable
        ?: run {
            logger.warn("Chat client is not available")
            throw ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "This service is currently disabled"
            )
        }
}