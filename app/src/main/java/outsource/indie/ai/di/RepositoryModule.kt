package outsource.indie.ai.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import outsource.indie.ai.data.GroqApi
import outsource.indie.ai.data.GroqApiHelper
import outsource.indie.ai.data.GroqRepository
import outsource.indie.ai.data.ModelRepository
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