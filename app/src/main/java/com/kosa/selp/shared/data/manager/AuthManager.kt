package com.kosa.selp.shared.data.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN, null)
        set(value) = prefs.edit { putString(ACCESS_TOKEN, value) }

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN, null)
        set(value) = prefs.edit { putString(REFRESH_TOKEN, value) }

    fun clearTokens() {
        prefs.edit { clear() }
    }

    companion object {
        private const val AUTH_PREFS = "auth_prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}
