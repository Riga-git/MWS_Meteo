package com.lri.currentweather


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h")
    val h1: Double = 0.0,
    @SerializedName("3h")
    val h3: Double = 0.0
)