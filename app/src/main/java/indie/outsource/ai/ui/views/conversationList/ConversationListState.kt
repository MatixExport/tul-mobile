package indie.outsource.ai.ui.views.conversationList

import indie.outsource.ai.model.Conversation

data class ConversationListState (
    val conversationsList : List<Conversation> = listOf()
)
