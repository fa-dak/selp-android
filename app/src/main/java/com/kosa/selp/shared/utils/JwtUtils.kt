package com.kosa.selp.shared.utils

import android.util.Base64
import com.google.gson.Gson
import com.kosa.selp.shared.data.model.UserInfo
import java.nio.charset.Charset

object JwtUtils {
    fun decodeUserInfo(token: String): UserInfo? {
        return try {
            val payload = token.split('.')[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE)
            val decodedJson = String(decodedBytes, Charset.defaultCharset())
            Gson().fromJson(decodedJson, UserInfo::class.java)
        } catch (e: Exception) {
            // 토큰 파싱 실패 시 null 반환
            e.printStackTrace()
            null
        }
    }
}
