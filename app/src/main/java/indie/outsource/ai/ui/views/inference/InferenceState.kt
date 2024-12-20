package indie.outsource.ai.ui.views.inference

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import indie.outsource.ai.model.Message

@Stable
data class InferenceState (
    val messages : List<Message> = listOf(),
    val isDialogOpen : Boolean = false,
    val conversationTitle : TextFieldValue = TextFieldValue(),
    val isLoading : Boolean = false
)