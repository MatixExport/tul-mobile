package indie.outsource.ai.model.response

import com.google.gson.annotations.SerializedName

data class GroqModel(
    @SerializedName("id")
    var name: String,

    @SerializedName("owned_by")
    var owner: String,
)
