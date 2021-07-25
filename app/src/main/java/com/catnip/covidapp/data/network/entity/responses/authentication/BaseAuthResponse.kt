package com.catnip.covidapp.data.network.entity.responses.authentication

import com.google.gson.annotations.SerializedName

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
data class BaseAuthResponse<T, E>(
    @field:SerializedName("success")
    val success: Boolean,
    @field:SerializedName("data")
    val data: T,
    @field:SerializedName("errors")
    val errors: E,
)