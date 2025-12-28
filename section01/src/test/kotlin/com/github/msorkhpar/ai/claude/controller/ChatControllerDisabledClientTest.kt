package com.github.msorkhpar.ai.claude.controller

import org.hamcrest.core.StringContains.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ChatController::class)
@AutoConfigureMockMvc
@TestPropertySource(
    properties = [
        "chat.client.ollama-docker.enabled=false"
    ]
)
class ChatControllerDisabledClientTest {
    
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return disabled message when ollama-docker client is disabled`() {
        mockMvc.perform(get("/api/ollama-docker").param("message", "Hello"))
            .andExpect(status().isServiceUnavailable)
            .andExpect(status().reason(containsString("is currently disabled")))
    }
}
