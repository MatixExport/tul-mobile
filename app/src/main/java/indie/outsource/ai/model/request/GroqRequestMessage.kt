package indie.outsource.ai.model.request

import com.google.gson.annotations.SerializedName

data class GroqRequestMessage (

    @SerializedName("role")
    var role : String,

    @SerializedName("content")
    var content : String,

)

