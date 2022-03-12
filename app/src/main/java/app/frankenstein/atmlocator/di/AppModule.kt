package app.frankenstein.atmlocator.di

import app.frankenstein.atmlocator.BuildConfig
import app.frankenstein.atmlocator.data.PoiApi
import app.frankenstein.atmlocator.data.Repository
import app.frankenstein.atmlocator.data.RepositoryImpl
import app.frankenstein.atmlocator.domain.PoiUseCase
import app.frankenstein.atmlocator.domain.PoiUseCaseImpl
import app.frankenstein.atmlocator.utils.CoroutineDispatchers
import app.frankenstein.atmlocator.utils.DefaultDispatchers
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.math.BigDecimal
import javax.inject.Singleton

@Module(includes = [AppSingletonBindModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDefaultDispatchers(): CoroutineDispatchers {
        return DefaultDispatchers
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): PoiApi {
        return retrofit.create(PoiApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(BigDecimalAdapter())
            .build()
    }

    class BigDecimalAdapter {
        @FromJson
        fun fromJson(string: String) = BigDecimal(string)

        @ToJson
        fun toJson(value: BigDecimal) = value.toString()
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppSingletonBindModule {

    @Binds
    abstract fun provideRepository(
        repository: RepositoryImpl
    ) : Repository

    @Binds
    abstract fun providePoiUseCase(
        useCase: PoiUseCaseImpl
    ) : PoiUseCase

}