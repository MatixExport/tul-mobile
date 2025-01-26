package indie.outsource.ai.data

import indie.outsource.ai.model.Model
import indie.outsource.ai.model.ModelType
import indie.outsource.ai.model.response.GroqModel
import indie.outsource.ai.model.request.GroqRequest
import indie.outsource.ai.model.request.GroqRequestMessage
import indie.outsource.ai.model.response.GroqCompletionResponse
import indie.outsource.ai.model.response.GroqModelListResponse
import retrofit2.Response
import javax.inject.Inject


interface ModelRepository {
    suspend fun getAllModels(): List<Model>
    suspend fun getCompletion(text:String,modelId: String): String
}

class GroqRepository @Inject constructor(
    private val api: GroqApi
) : ModelRepository {

    override suspend fun getAllModels(): List<Model> {
        val result : Response<GroqModelListResponse> = api.getModels(getTokenString());
        if(result.isSuccessful){
            val responseBody : GroqModelListResponse = result.body()!!
            return responseBody.models.map {
                groqModel: GroqModel -> Model(
                    groqModel.name,
                    groqModel.owner,
                    ModelType.TEXT
                )
            }
        }
        throw RuntimeException();
    }

    override suspend fun getCompletion(text: String,modelId: String): String {
        val result = api.getCompletion(
            getTokenString(),
            GroqRequest(
                listOf<GroqRequestMessage>(
                    GroqRequestMessage("user", text)
                ),
                400,
                modelId
                ),
        )
        if(result.isSuccessful){
            val responseBody : GroqCompletionResponse = result.body()!!
            return responseBody.choices[0].message.content
        }
        println(result.errorBody().toString())
        throw RuntimeException()
    }

    private fun getTokenString() : String{
        return "Bearer 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";
    }
}