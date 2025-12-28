package com.github.msorkhpar.ai.claude.configuration

import org.springframework.ai.chat.client.advisor.api.Advisor

data class ChatClientAdvisors(val advisors: List<Advisor>)
