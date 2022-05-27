package com.bangkit.intermediate.storyappfinal.core.feature.auth.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.service.UserPreferenceService
import com.bangkit.intermediate.storyappfinal.core.feature.auth.domain.response.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user")

class UserPreference @Inject constructor(
    @ApplicationContext context: Context
): UserPreferenceService {

    private val userDataStore = context.dataStore

    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USER_ID = stringPreferencesKey("user_id")

    override fun getUser(): Flow<User?> = userDataStore.data.map{ pref ->
        User(
                name = pref[USER_NAME] ?: "",
                token = pref[USER_TOKEN] ?: "",
                userId = pref[USER_ID] ?: ""
            )
    }

    override suspend fun setUser(user: User?) {
        if (user != null) {
            userDataStore.edit {
                it[USER_NAME] = user.name
                it[USER_TOKEN] = user.token
                it[USER_ID] = user.userId
            }
        } else {
            userDataStore.edit {
                it.clear()
            }
        }
    }
}