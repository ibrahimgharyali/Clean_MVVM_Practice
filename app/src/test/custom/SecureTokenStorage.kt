package com.ibrahimgharyali.mypracticeapplication.custom

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureTokenStorage(context: Context) : TokenProvider {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_tokens_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getAccessToken(): String? = sharedPreferences.getString("ACCESS_TOKEN", null)

    override fun getRefreshToken(): String? = sharedPreferences.getString("REFRESH_TOKEN", null)

    override fun saveTokens(accessToken: String, refreshToken: String) {
        sharedPreferences.edit()
            .putString("ACCESS_TOKEN", accessToken)
            .putString("REFRESH_TOKEN", refreshToken)
            .apply()
    }

    override fun clearTokensAndForceLogout() {
        sharedPreferences.edit().clear().apply()
        // Trigger global Logout/Session Expired Event via SharedFlow/EventBus
    }
}