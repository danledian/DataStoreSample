package com.hs.datastoresample.data.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.createDataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.hs.datastoresample.data.bean.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream

class UserProtoDataStore(context: Context): UserDataStore {

    companion object{
        const val FILENAME = "user.pb"
    }

    private val dataStore by lazy {
        context.createDataStore(
            fileName = FILENAME,
            serializer = UserSerializer
        )
    }

    object UserSerializer: Serializer<User> {

        override val defaultValue: User
            get() = User.getDefaultInstance()

        override fun readFrom(input: InputStream): User {
            try {
                return User.parseFrom(input)
            }catch (e: InvalidProtocolBufferException){
                throw CorruptionException("Cannot read proto.", e)
            }
        }

        override fun writeTo(t: User, output: OutputStream) {
            t.writeTo(output)
        }
    }

    override suspend fun setUsername(name: String){
        dataStore.updateData {
            it.toBuilder()
            .setUsername(name)
            .build()
        }
    }

    override fun getUsername(): Flow<String?>{
        return dataStore.data
        .map {
            it.username
        }
    }

    override fun toString(): String {
        return "[UserProtoDataStore]"
    }

}