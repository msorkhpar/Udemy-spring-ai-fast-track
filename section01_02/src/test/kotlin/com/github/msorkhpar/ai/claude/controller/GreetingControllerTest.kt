package com.github.msorkhpar.ai.claude.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(GreetingController::class)
class GreetingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `hi should return greeting with provided language`() {
        val lang = "Kotlin"

        mockMvc.perform(MockMvcRequestBuilders.get("/greeting/hi/$lang"))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.lang").value("Hello $lang"))
    }


}