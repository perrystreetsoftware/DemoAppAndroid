package com.example.networklogic
import com.example.domainmodels.CountryDetailsDTO
import com.example.domainmodels.CountryListDTO
import com.example.interfaces.ITravelAdvisoriesApi
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import java.io.IOException


class TravelAdvisoriesApi(val moshi: Moshi): ITravelAdvisoriesApi {
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
                Observable.just<Response>(response).map {
                    jsonAdapter.fromJson(it.body?.string()) ?: CountryListDTO.EMPTY
                }
            } catch (e: IOException) {
                Observable.error(e)
            } catch (e: java.lang.IllegalStateException) {
                Observable.error(e)
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun getCountryDetails(regionCode: String): Observable<CountryDetailsDTO> {
        val client = OkHttpClient()
        val url = "https://www.scruff.com/advisories/${regionCode}/index.json"
        val request: Request = Builder()
            .url(url)
            .build()

        val jsonAdapter: JsonAdapter<CountryDetailsDTO> = moshi.adapter(CountryDetailsDTO::class.java)

        return Observable.defer {
            try {
                val response: Response = client.newCall(request).execute()
                Observable.just<Response>(response).map {
                    jsonAdapter.fromJson(it.body.toString()) ?: CountryDetailsDTO.EMPTY
                }
            } catch (e: IOException) {
                Observable.error(e)
            }
        }.subscribeOn(Schedulers.io())
    }
}