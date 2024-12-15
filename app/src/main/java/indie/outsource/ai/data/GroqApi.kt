package indie.outsource.ai.data

import indie.outsource.ai.model.request.GroqRequest
import indie.outsource.ai.model.response.GroqCompletionResponse
import indie.outsource.ai.model.response.GroqModelListResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApi {
    @GET("models")
    suspend fun getModels(
        @Header("Authorization") token: String
    ) : Response<GroqModelListResponse>

    @POST("chat/completions")
    suspend fun getCompletion(
        @Header("Authorization") token: String,
        @Body request: GroqRequest
    ) : Response<GroqCompletionResponse>

    companion object{
        const val BASE_URL = "https://api.groq.com/openai/v1/"
    }
}

object GroqApiHelper{
    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(GroqApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}