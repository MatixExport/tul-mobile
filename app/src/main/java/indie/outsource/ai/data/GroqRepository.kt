package indie.outsource.ai.data

import indie.outsource.ai.model.GroqModel
import retrofit2.Response


interface ModelRepository {
    suspend fun getAllModels(): List<GroqModel>
}

class GroqRepository constructor(
    private val api: GroqApi
) : ModelRepository {

    override suspend fun getAllModels(): List<GroqModel> {
        val result : Response<List<GroqModel>> = api.getModels();
        if(result.isSuccessful){
            return result.body()!!;
        }
        throw RuntimeException();
    }
}