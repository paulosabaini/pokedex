package org.sabaini.pokedex.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.sabaini.pokedex.data.local.PokemonDao
import org.sabaini.pokedex.data.local.PokemonDatabase
import org.sabaini.pokedex.data.local.PokemonInfoDao
import org.sabaini.pokedex.data.local.PokemonLocalDataSource
import org.sabaini.pokedex.data.remote.PokemonApi
import org.sabaini.pokedex.data.remote.PokemonRemoteDataSource
import org.sabaini.pokedex.data.repository.PokemonRepositoryImpl
import org.sabaini.pokedex.domain.repository.PokemonRepository
import org.sabaini.pokedex.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonDatabase(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            "pokemon_db",
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providePokemonDao(database: PokemonDatabase): PokemonDao {
        return database.PokemonDao()
    }

    @Provides
    fun providePokemonInfoDao(database: PokemonDatabase): PokemonInfoDao {
        return database.PokemonInfoDao()
    }

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    fun provideCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Singleton
    @Provides
    fun providePokemonRepository(
        pokemonRemoteDataSource: PokemonRemoteDataSource,
        pokemonLocalDataSource: PokemonLocalDataSource,
        externalScope: CoroutineScope,
    ): PokemonRepository {
        return PokemonRepositoryImpl(pokemonRemoteDataSource, pokemonLocalDataSource, externalScope)
    }

    @Singleton
    @Provides
    fun providePokemonRemoteDataSource(
        pokemonApi: PokemonApi,
        ioDispatcher: CoroutineDispatcher,
    ): PokemonRemoteDataSource {
        return PokemonRemoteDataSource(pokemonApi, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providePokemonLocalDataSource(
        pokemonDao: PokemonDao,
        pokemonInfoDao: PokemonInfoDao,
        ioDispatcher: CoroutineDispatcher,
    ): PokemonLocalDataSource {
        return PokemonLocalDataSource(pokemonDao, pokemonInfoDao, ioDispatcher)
    }

    @Singleton
    @Provides
    fun providePokemonApi(): PokemonApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokemonApi::class.java)
    }
}
