package indie.outsource.ai.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import indie.outsource.ai.model.AccountData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AccountDataRepository {
    suspend fun loadAccountData() : AccountData
    suspend fun updateAccountData(data:AccountData) : Boolean
}

class AccountDataRepositoryImpl @Inject constructor(
    private val db : FirebaseFirestore,
    private val auth: FirebaseAuth
) : AccountDataRepository {
    private val collectionName = "accounts"


    override suspend fun loadAccountData(): AccountData {
        val userId = auth.currentUser?.uid ?: throw RuntimeException("User does not exist")
        val getResult = db.collection(collectionName)
            .whereEqualTo("userId", userId)
            .limit(1)
            .get()
            .await()
        if(getResult.isEmpty){
            val newAccountData : AccountData =   AccountData(
                "",userId, arrayOf<String>().toList()
            )
            val postResult = db.collection(collectionName)
                .add(newAccountData)
                .await()
            newAccountData.documentId = postResult.id
            return newAccountData
        }
        println("Loaded from account")
        println(getResult.toString())
        return getResult.toObjects(AccountData::class.java).first()

    }

    override suspend fun updateAccountData(data: AccountData): Boolean {
        val result = db.collection(collectionName)
            .document(data.documentId)
            .set(data)
        if(result.isSuccessful){
            return true
        }else{
            throw RuntimeException(result.exception?.message.toString())
        }
    }
}