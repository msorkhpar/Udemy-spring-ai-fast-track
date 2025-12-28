package com.github.msorkhpar.ai.claude.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "chat.client")
data class ChatClientProperties(
    val claude: ClientConfig = ClientConfig(),
    val ollama: ClientConfig = ClientConfig(),
    val ollamaDocker: ClientConfig = ClientConfig()
) {
    data class ClientConfig(
        val enabled: Boolean = true
    )
}
