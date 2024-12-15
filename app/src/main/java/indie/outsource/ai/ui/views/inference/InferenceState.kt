package indie.outsource.ai.ui.views.inference

import indie.outsource.ai.model.Message

data class InferenceState (
    val messages : List<Message> = listOf(),
)