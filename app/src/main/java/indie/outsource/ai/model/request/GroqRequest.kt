package indie.outsource.ai.model.request

import com.google.gson.annotations.SerializedName

data class GroqRequest (

    @SerializedName("messages")
    var messages : List<GroqRequestMessage>,

    @SerializedName("max_tokens")
    var tokenLimit : Int,

    @SerializedName("model")
    var model : String,

)
