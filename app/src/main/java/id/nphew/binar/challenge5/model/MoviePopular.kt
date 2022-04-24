package id.nphew.binar.challenge5.model

import com.google.gson.annotations.SerializedName

data class MoviePopular(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MoviePopularItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)