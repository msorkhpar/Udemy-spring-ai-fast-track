package com.github.msorkhpar.ai.claude.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.client.ChatClientRequest
import org.springframework.ai.chat.client.ChatClientResponse
import org.springframework.ai.chat.client.advisor.api.CallAdvisor
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain

class TokenUsageAuditAdvisor : CallAdvisor {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TokenUsageAuditAdvisor::class.java)
        private val NAME: String = TokenUsageAuditAdvisor::class.java.simpleName
        private const val ORDER = 0
    }

    override fun adviseCall(
        chatClientRequest: ChatClientRequest,
        callAdvisorChain: CallAdvisorChain
    ): ChatClientResponse = callAdvisorChain.nextCall(chatClientRequest).also { response ->
        response.chatResponse()?.metadata?.usage?.let { usage ->
            logger.info("Token usage details : {}", usage)
        }
    }

    override fun getName(): String = NAME

    override fun getOrder(): Int = ORDER


}