package com.hs.datastoresample.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferenceDataStore(context: Context): UserDataStore {

    private val dataStore by lazy {
        context.createDataStore(
            name = USER
        )
    }

    companion object{
        const val USER = "user"
        private const val USERNAME = "username"
        val NAME = preferencesKey<String>(USERNAME)
    }


    override fun getUsername(): Flow<String?>{
        return dataStore.data
        .map {
            it[NAME]
        }
    }

    override suspend fun setUsername(name: String){
        dataStore.edit {
            it[NAME] = name
        }
    }

    override fun toString(): String {
        return "[UserPreferenceDataStore]"
    }
}