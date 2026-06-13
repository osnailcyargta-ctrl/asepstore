package com.asepstore.app

data class AppItem(
    val id: Int,
    val name: String,
    val shortDesc: String,
    val fullDesc: String,
    val size: String,
    val version: String,
    val emoji: String,
    val apkUrl: String,
    val packageName: String,
    val features: List<String>
)
