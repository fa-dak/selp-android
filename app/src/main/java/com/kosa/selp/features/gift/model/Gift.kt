package com.kosa.selp.features.gift.model

data class Gift(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val description: String,
    val price: Int
)