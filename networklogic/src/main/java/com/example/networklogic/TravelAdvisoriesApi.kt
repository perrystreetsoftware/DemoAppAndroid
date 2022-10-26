package com.example.networklogic
import com.example.dtos.CountryDetailsDTO
import com.example.dtos.CountryListDTO
import com.example.dtos.ServerStatusDTO
import com.example.interfaces.ITravelAdvisoriesApi
import com.example.interfaces.TravelAdvisoryApiError
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class TravelAdvisoriesApi(val moshi: Moshi): ITravelAdvisoriesApi {
    override fun getServerStatus(): Observable<ServerStatusDTO> {
        val client = OkHttpClient()
        val url = "https://status.scruff.com/index.json"
        val request: Request = Builder()
            .url(url)
            .build()

        val jsonAdapter: JsonAdapter<ServerStatusDTO> = moshi.adapter(ServerStatusDTO::class.java)

        return Observable.defer {
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Observable.just<Response>(response).map {
                        jsonAdapter.fromJson(it.body!!.string()) ?: ServerStatusDTO.EMPTY
                    }
                } else {
                    Observable.error(TravelAdvisoryApiError.fromStatusCode(response.code))
                }
            } catch (e: IOException) {
                Observable.error(TravelAdvisoryApiError.Other(e))
            } catch (e: java.lang.IllegalStateException) {
                Observable.error(TravelAdvisoryApiError.Other(e))
            }
        }
            .delay(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun getForbiddenApi(): Completable {
        val client = OkHttpClient()
        val url = "https://httpstat.us/403"
        val request: Request = Builder()
            .url(url)
            .build()

        return Completable.defer {
            try {
                val response: Response = client.newCall(request).execute()
                Completable.error(TravelAdvisoryApiError.fromStatusCode(response.code))
            } catch (e: IOException) {
                Completable.error(TravelAdvisoryApiError.Other(e))
            } catch (e: java.lang.IllegalStateException) {
                Completable.error(TravelAdvisoryApiError.Other(e))
            }
        }
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun getCountryList(): Observable<CountryListDTO> {
        val client = OkHttpClient()
        val url = "https://www.scruff.com/advisories/index.json"
        val request: Request = Builder()
            .url(url)
            .build()

        val jsonAdapter: JsonAdapter<CountryListDTO> = moshi.adapter(CountryListDTO::class.java)

        return Observable.defer {
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Observable.just<Response>(response).map {
                        jsonAdapter.fromJson(it.body!!.string()) ?: CountryListDTO.EMPTY
                    }
                } else {
                    Observable.error(TravelAdvisoryApiError.fromStatusCode(response.code))
                }
            } catch (e: IOException) {
                Observable.error(TravelAdvisoryApiError.Other(e))
            } catch (e: java.lang.IllegalStateException) {
                Observable.error(TravelAdvisoryApiError.Other(e))
            }
        }
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun getCountryDetails(regionCode: String): Observable<CountryDetailsDTO> {
        val client = OkHttpClient()
        var url: String = if (regionCode == "xx") {
            "https://httpstat.us/404"
        } else {
            "https://www.scruff.com/advisories/${regionCode}/index.json"
        }
        val request: Request = Builder()
            .url(url)
            .build()

        val jsonAdapter: JsonAdapter<CountryDetailsDTO> = moshi.adapter(CountryDetailsDTO::class.java)

        return Observable.defer {
            try {
                val response: Response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Observable.just<Response>(response).map {
                        jsonAdapter.fromJson(it.body!!.string()) ?: CountryDetailsDTO.EMPTY
                    }
                } else {
                    Observable.error(TravelAdvisoryApiError.fromStatusCode(response.code))
                }
            } catch (e: IOException) {
                Observable.error(TravelAdvisoryApiError.Other(e))
            }
        }
            .delay(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

}