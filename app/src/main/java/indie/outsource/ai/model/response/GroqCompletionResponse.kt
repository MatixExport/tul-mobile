package indie.outsource.ai.model.response

import com.google.gson.annotations.SerializedName
import indie.outsource.ai.model.request.GroqRequestMessage


data class GroqCompletionResponseChoice(
    @SerializedName("index")
    var index : Int,

    @SerializedName("message")
    var message: GroqRequestMessage
)

data class GroqCompletionResponse (
    @SerializedName("choices")
    var choices: List<GroqCompletionResponseChoice>
)
