package indie.outsource.ai.data

import indie.outsource.ai.model.GroqModel
import indie.outsource.ai.model.Message
import indie.outsource.ai.model.request.GroqRequest
import indie.outsource.ai.model.request.GroqRequestMessage
import indie.outsource.ai.model.response.GroqCompletionResponse
import retrofit2.Response
import javax.inject.Inject


interface ModelRepository {
    suspend fun getAllModels(): List<GroqModel>
    suspend fun getCompletion(text:String): String
}

class GroqRepository @Inject constructor(
    private val api: GroqApi
) : ModelRepository {

    override suspend fun getAllModels(): List<GroqModel> {
        val result : Response<List<GroqModel>> = api.getModels(getTokenString());
        if(result.isSuccessful){
            return result.body()!!;
        }
        throw RuntimeException();
    }

    override suspend fun getCompletion(text: String): String {
        val result = api.getCompletion(
            getTokenString(),
            GroqRequest(
                listOf<GroqRequestMessage>(
                    GroqRequestMessage("user", text)
                ),
                400,
                "llama3-8b-8192"
                ),
        )
        if(result.isSuccessful){
            val responseBody : GroqCompletionResponse = result.body()!!
            return responseBody.choices[0].message.content
        }
        throw RuntimeException()
    }

    private fun getTokenString() : String{
        return "Bearer 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";
    }
}