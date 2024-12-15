package indie.outsource.ai.data

import indie.outsource.ai.model.GroqModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface GroqApi {
    @GET("/models")
    suspend fun getModels() : Response<List<GroqModel>>
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