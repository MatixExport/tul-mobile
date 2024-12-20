package indie.outsource.ai.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import indie.outsource.ai.data.ConversationRepository
import indie.outsource.ai.data.ConversationRepositoryImpl
import indie.outsource.ai.data.GroqApi
import indie.outsource.ai.data.GroqApiHelper
import indie.outsource.ai.data.GroqRepository
import indie.outsource.ai.data.ModelRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {



    @Singleton
    @Provides
    fun provideGroqApi(): GroqApi {
        return GroqApiHelper.getInstance().create(GroqApi::class.java)
    }

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideModelRepository(
        api: GroqApi
    ): ModelRepository = GroqRepository(api)

    @Singleton
    @Provides
    fun provideConversationRepository(
        db: FirebaseFirestore,
        auth:FirebaseAuth
    ): ConversationRepository = ConversationRepositoryImpl(db,auth)


}