
package com.buuzz.donorconnect.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

//    @Singleton
//    @Provides
//    fun getRoomDatabase(@ApplicationContext context: Context): LocalDatabase =
//        Room.databaseBuilder(context, LocalDatabase::class.java, "local_database")
//            .fallbackToDestructiveMigration().allowMainThreadQueries().build()
//
//
//    @Singleton
//    @Provides
//    fun getPatientDao(localDatabase: LocalDatabase) = localDatabase.getPatientDao()
//
//
//    @Singleton
//    @Provides
//    fun getTestResultDao(localDatabase: LocalDatabase) = localDatabase.getTestResultDao()

}