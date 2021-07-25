package com.catnip.covidapp.data.network.entity.responses.kawalcorona

import com.google.gson.annotations.SerializedName


data class IndonesiaCaseResponse(

	@field:SerializedName("meninggal")
	val meninggal: String? = null,

	@field:SerializedName("positif")
	val positif: String? = null,

	@field:SerializedName("sembuh")
	val sembuh: String? = null,

	@field:SerializedName("dirawat")
	val dirawat: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)
