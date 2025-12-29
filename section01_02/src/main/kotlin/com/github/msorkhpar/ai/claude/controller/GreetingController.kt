package com.github.msorkhpar.ai.claude.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class Greeting(val lang: String)

@RestController
@RequestMapping("/greeting")
class GreetingController {

    @GetMapping("/hi/{lang}")
    fun hi(@PathVariable lang: String): Greeting {
        return Greeting("Hello $lang")
    }
}