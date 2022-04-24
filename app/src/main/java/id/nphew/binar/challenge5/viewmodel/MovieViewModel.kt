package id.nphew.binar.challenge5.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.nphew.binar.challenge5.model.MoviePopular
import id.nphew.binar.challenge5.model.MoviePopularItem
import id.nphew.binar.challenge5.service.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel(private val apiService: ApiService) : ViewModel() {

        private val _dataError = MutableLiveData<String>()
        val dataError: LiveData<String> get() = _dataError

        private val _detailSuccess = MutableLiveData<MoviePopularItem>()
        val detailSuccess: LiveData<MoviePopularItem> get() = _detailSuccess

        private val _movie = MutableLiveData<MoviePopular>()
        val movie: LiveData<MoviePopular> get() = _movie

        fun getAllMovies() {
            apiService.getMoviePopular("1277127d2eed7437027f5d5952fa28c2")
                .enqueue(object : Callback<MoviePopular> {
                    override fun onResponse(
                        call: Call<MoviePopular>,
                        response: Response<MoviePopular>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                _movie.value = response.body()
                            } else {
                                _dataError.postValue("Data Kosong")
                            }
                        } else {
                            _dataError.postValue("Pengambilan Data Gagal")
                        }
                        Log.w("onResponse", "Berhasil")
                    }

                    override fun onFailure(call: Call<MoviePopular>, t: Throwable) {
                        _dataError.postValue("Server Bermasalah")
                        Log.w("onFailure", "Gagal")
                    }

                })
        }

    fun getDetailMovie(movieId: Int) {
        apiService.getDetailMovie(movieId = movieId, key = "1277127d2eed7437027f5d5952fa28c2")
            .enqueue(object : Callback<MoviePopularItem> {
                override fun onResponse(
                    call: Call<MoviePopularItem>,
                    response: Response<MoviePopularItem>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            _detailSuccess.postValue(response.body())
                        } else {
                            _dataError.postValue("Datanya kosong")
                        }
                    } else {
                        _dataError.postValue("Pengambilan data gagal")
                    }
                }

                override fun onFailure(call: Call<MoviePopularItem>, t: Throwable) {
                    _dataError.postValue("Server bermasalah")
                }

            })
    }

}