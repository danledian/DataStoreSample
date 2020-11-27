package com.hs.datastoresample.data.datastore

import kotlinx.coroutines.flow.Flow

interface UserDataStore {

    suspend fun setUsername(name: String)

    fun getUsername(): Flow<String?>
}