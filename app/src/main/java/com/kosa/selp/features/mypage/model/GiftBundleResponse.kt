package com.kosa.selp.features.mypage.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GiftBundleResponse(
    @SerializedName("giftBundleId")
    val giftBundleId: Long,
    @SerializedName("createdDate")
    val createdDate: String,
    @SerializedName("eventId")
    val eventId: Long,
    @SerializedName("eventType")
    val eventType: String,
    @SerializedName("eventDate")
    val eventDate: String, // LocalDate는 String으로 받습니다.
    @SerializedName("eventName")
    val eventName: String?,
    @SerializedName("receiverInfoId")
    val receiverInfoId: Long,
    @SerializedName("receiverNickname")
    val receiverNickname: String?,
    @SerializedName("relationship")
    val relationship: String,
    @SerializedName("products")
    val products: List<Product>
) : Parcelable {
    @Parcelize
    data class Product(
        @SerializedName("productId")
        val productId: Long,
        @SerializedName("category")
        val category: String?, // ProductCategory는 String으로 받습니다.
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: Long,
        @SerializedName("imagePath")
        val imagePath: String,
        @SerializedName("detailPath")
        val detailPath: String
    ) : Parcelable
}
