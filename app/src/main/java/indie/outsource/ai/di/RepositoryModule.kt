package indie.outsource.ai.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideModelRepository(
        api: GroqApi
    ): ModelRepository = GroqRepository(api)


}