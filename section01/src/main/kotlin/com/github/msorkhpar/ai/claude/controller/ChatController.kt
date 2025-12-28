package com.github.msorkhpar.ai.claude.controller

import com.github.msorkhpar.ai.claude.extensions.getOrThrow
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.ObjectProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ChatController(
    private val chatClientProvider: ObjectProvider<ChatClient>,
    @param:Qualifier("ollamaChatClient") private val ollamaChatClientProvider: ObjectProvider<ChatClient>,
    @param:Qualifier("ollamaDockerChatClient") private val ollamaDockerChatClientProvider: ObjectProvider<ChatClient>
) {

    @GetMapping("", "/")
    fun chatWithDefaultLLM(@RequestParam message: String): String =
        executeChat(chatClientProvider, message)

    @GetMapping("/ollama")
    fun chatWithOllama(@RequestParam message: String): String =
        executeChat(ollamaChatClientProvider, message)

    @GetMapping("/ollama-docker")
    fun chatWithOllamaInDocker(@RequestParam message: String): String =
        executeChat(ollamaDockerChatClientProvider, message)

    private fun executeChat(provider: ObjectProvider<ChatClient>, message: String): String =
        provider.getOrThrow()
            .prompt(message)
            .call()
            .content()
            ?.takeIf { it.isNotBlank() }
            ?: "No response received from the model."
}