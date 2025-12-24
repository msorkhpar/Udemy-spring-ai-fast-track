package com.github.msorkhpar.ai.claude.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ChatController(private val chatClient: ChatClient) {

    @GetMapping("/chat")
    fun chat(@RequestParam message: String): String? {
        return chatClient.prompt(message).call().content()
    }
}