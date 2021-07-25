package com.catnip.covidapp.data.network.entity.responses.news

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    @field:SerializedName("response")
    val response: Response? = null
) : Parcelable

@Parcelize
data class Response(

    @field:SerializedName("userTier")
    val userTier: String? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("startIndex")
    val startIndex: Int? = null,

    @field:SerializedName("pages")
    val pages: Int? = null,

    @field:SerializedName("pageSize")
    val pageSize: Int? = null,

    @field:SerializedName("orderBy")
    val orderBy: String? = null,

    @field:SerializedName("currentPage")
    val currentPage: Int? = null,

    @field:SerializedName("results")
    val results: List<News>? = null,

    @field:SerializedName("status")
    val status: String? = null
) : Parcelable

@Parcelize
data class News(

    @field:SerializedName("sectionName")
    val sectionName: String? = null,

    @field:SerializedName("pillarName")
    val pillarName: String? = null,

    @field:SerializedName("webPublicationDate")
    val webPublicationDate: String? = null,

    @field:SerializedName("apiUrl")
    val apiUrl: String? = null,

    @field:SerializedName("webUrl")
    val webUrl: String? = null,

    @field:SerializedName("isHosted")
    val isHosted: Boolean? = null,

    @field:SerializedName("pillarId")
    val pillarId: String? = null,

    @field:SerializedName("webTitle")
    val webTitle: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("sectionId")
    val sectionId: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("fields")
    val fields: Fields? = null
) : Parcelable

@Parcelize
data class Fields(
    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,
    @field:SerializedName("trailText")
    val trailText: String? = null,
) : Parcelable