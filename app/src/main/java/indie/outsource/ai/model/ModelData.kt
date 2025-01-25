package indie.outsource.ai.model

import com.google.firebase.firestore.DocumentId

data class ModelData(
    @DocumentId
    var documentId: String = "",
    var modelId:String = "",
    var likedBy:List<String> = listOf()
)
