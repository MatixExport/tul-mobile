package indie.outsource.ai.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import indie.outsource.ai.model.Conversation
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface ConversationRepository {
    suspend fun loadConversationDetails(uuid:String) : Conversation
    suspend fun loadAllConversationsHeaders() : List<Conversation>
    suspend fun loadRecentConversations(count:Int) : List<Conversation>
    suspend fun saveConversation(conversation: Conversation) : Conversation
    suspend fun removeConversation(conversation: Conversation) : Boolean
}

class ConversationRepositoryImpl @Inject constructor(
    private val db : FirebaseFirestore,
    private val auth: FirebaseAuth
) : ConversationRepository {
    private val collectionName = "conversations"

    override suspend fun loadConversationDetails(uuid: String): Conversation {
        val result = db.collection(collectionName)
            .document(uuid)
            .get()
            .await()
        return result.toObject(Conversation::class.java)!!
    }

    override suspend fun loadAllConversationsHeaders(): List<Conversation> {
        //limit fields
        println("current user uid: ${auth.currentUser?.uid}")
        val result = db.collection(collectionName)
            .whereEqualTo("userId", auth.currentUser?.uid)
            .get()
            .await()

        return result.toObjects(Conversation::class.java)

    }

    override suspend  fun loadRecentConversations(count: Int): List<Conversation> {
        //limit fields
        val result = db.collection(collectionName)
            .whereEqualTo("userId", auth.currentUser?.uid)
            .orderBy("lastVisited", Query.Direction.DESCENDING)
            .limit(count.toLong())
            .get()
            .await()

        return result.toObjects(Conversation::class.java)

    }

    override suspend fun saveConversation(conversation: Conversation): Conversation {
        if(auth.currentUser == null){
            throw RuntimeException("No auth")
        }
        conversation.userId = auth.currentUser!!.uid

        if(conversation.documentId.isEmpty()){
            return createConversation(conversation)
        }
        return modifyConversation(conversation)
    }

    private suspend fun createConversation(conversation: Conversation) : Conversation{
        val result = db.collection(collectionName)
            .add(conversation)
            .await()
        conversation.documentId = result.id
        return conversation
    }

    private suspend fun modifyConversation(conversation: Conversation) : Conversation{
        val result = db.collection(collectionName)
            .document(conversation.documentId)
            .set(conversation)
        if(result.isSuccessful){
            return conversation
        }else{
            throw RuntimeException(result.exception?.message.toString())
        }
    }

    override suspend fun removeConversation(conversation: Conversation): Boolean {
        TODO("Not yet implemented")
    }
}