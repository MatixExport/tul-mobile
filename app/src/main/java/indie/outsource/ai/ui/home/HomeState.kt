package indie.outsource.ai.ui.home

import indie.outsource.ai.model.Message

data class HomeState (
    val messages : List<Message> = listOf()
)