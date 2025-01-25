package indie.outsource.ai.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import indie.outsource.ai.model.ModelData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface ModelDataRepository {
    suspend fun loadModelsData() : List<ModelData>
    suspend fun toggleLikeModel(documentId:String,modelId: String)
}

class ModelDataRepositoryImpl @Inject constructor(
    private val db : FirebaseFirestore,
    private val auth: FirebaseAuth
) : ModelDataRepository {
    private val collectionName = "models"

    override suspend fun loadModelsData(): List<ModelData> {
        val result = db.collection(collectionName)
            .get()
            .await()
        return result.toObjects(ModelData::class.java)
    }

    private fun createModelData(modelData: ModelData){
        val result = db.collection(collectionName)
            .add(modelData)
    }


    override suspend fun toggleLikeModel(documentId: String,modelId:String) {
        val userId = auth.currentUser?.uid ?: throw RuntimeException("User does not exist")

        if(documentId.isNotEmpty()){
            val modelRef = db.collection(collectionName).document(documentId)
            db.runTransaction { transaction ->
                val model = transaction.get(modelRef).toObject(ModelData::class.java)
                if(model == null){
                    createModelData(
                        ModelData("",modelId, listOf(userId).toList())
                    )
                }else{
                    val likedBy = model.likedBy.toMutableList()
                    if (likedBy.contains(userId)) {
                        likedBy.remove(userId)
                    } else {
                        likedBy.add(userId)
                    }
                    transaction.update(modelRef, "likedBy", likedBy)
                }

            }.await()
            return
        }
        db.runTransaction { transaction ->
            createModelData(
                ModelData("",modelId, listOf(userId).toList())
            )
        }.await()
    }


}