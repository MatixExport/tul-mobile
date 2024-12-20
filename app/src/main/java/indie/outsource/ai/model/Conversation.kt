package indie.outsource.ai.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Conversation (
    @DocumentId
    var documentId: String = "",
    var userId: String = "",
    var title : String = "",
    var messages : List<Message> = listOf(),
    var models : List<String> = listOf(),
    var lastVisited: Timestamp = Timestamp.now()
)
