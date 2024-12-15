package indie.outsource.ai.model.response

import com.google.gson.annotations.SerializedName

data class GroqModelListResponse (
    @SerializedName("object")
    var type:String,

    @SerializedName("data")
    var models: List<GroqModel>
)

