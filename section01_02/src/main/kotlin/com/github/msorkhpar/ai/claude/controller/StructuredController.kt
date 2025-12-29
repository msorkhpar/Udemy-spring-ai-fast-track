package com.github.msorkhpar.ai.claude.controller

import com.github.msorkhpar.ai.claude.extensions.getOrThrow
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.converter.MapOutputConverter
import org.springframework.beans.factory.ObjectProvider
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/structured")
class StructuredController(
    private val claudeChatClientProvider: ObjectProvider<ChatClient>
) {

    @GetMapping("", "/")
    fun top5Movies(): ResponseEntity<Map<String, Any>> {
        val converter = MapOutputConverter()

        val systemMessage = """
            You are a movie expert with access to Rotten Tomatoes, IMDB and Metacritic.
             You will provider the list of top 5 movies of all time with their ratings and release year based on your own assessment.

            ${converter.format}
        """.trimIndent()

        val result = claudeChatClientProvider.getOrThrow()
            .prompt()
            .system(systemMessage)
            .call()
            .entity(converter)

        return ResponseEntity.ok(result)
    }

}
