package indie.outsource.ai.model

import com.google.firebase.firestore.DocumentId

data class AccountData (
    @DocumentId
    var documentId: String = "",
    var userId: String = "",
    var usedModels: List<String> = listOf()
)