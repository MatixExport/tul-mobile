package indie.outsource.ai.ui.views.home

import indie.outsource.ai.model.Conversation

data class HomeState (
    val conversationsList : List<Conversation> = listOf()
)
