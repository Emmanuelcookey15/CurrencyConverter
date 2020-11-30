package com.cookey.emmanuel.currencyconverter.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cookey.emmanuel.currencyconverter.Countries
import com.cookey.emmanuel.currencyconverter.api.ApiService
import com.cookey.emmanuel.currencyconverter.persistence.Currency
import com.cookey.emmanuel.currencyconverter.persistence.CountryCurrencyDao
import com.cookey.emmanuel.currencyconverter.persistence.CurrencyDatabase
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder
import kotlin.coroutines.CoroutineContext


class CurrencyRepository constructor() {

    var mApiService: ApiService? = null
    private val currencyLiveData = MutableLiveData<List<Currency>>()
    var rateOfSecondCurrency = 0f

    companion object {
        private var currencyRepository: CurrencyRepository? = null

        @get:Synchronized
        val instance: CurrencyRepository
            get() {
                if (currencyRepository == null) currencyRepository =
                    CurrencyRepository()
                return currencyRepository!!
            }
    }

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mApiService = retrofit.create<ApiService>(ApiService::class.java)
    }

    fun getCurrency(): LiveData<List<Currency>> {
        return currencyLiveData
    }

    fun fetchCurrencyByDate(time: String, involvedCurrency: String) : Float {

        val call: Call<JsonObject> = mApiService!!.getCurrencyHistory(time, "07dd79ddc7f86314dd8f391e87fdf958", involvedCurrency)

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("ResponseHistoryStatus", "Failed: " + t.message)
                rateOfSecondCurrency = 0f
                t.printStackTrace();
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (!response.isSuccessful){
                    Log.d("ResponseHistoryStatus", "Unsuccessful: ${response.code()}" + response.body())
                    if (response.message() == "null"){
                        return
                    }else{
                        Log.d("ResponseHistoryStatus", "${response.message()}")
                    }
                    rateOfSecondCurrency = 0f
                }

                if (response.isSuccessful) {
                    val getting = response.body()
                    Log.d("ResponseHistoryStatus", getting.toString())
                    val data = getting!!.get("rates").asJsonObject
                    val allAmount = ArrayList<Float>()

                    for (dataKey in data.keySet()) {
                        allAmount.add(data.get(dataKey).asFloat)
                    }

                    if (allAmount.size > 1) {
                        rateOfSecondCurrency = (1f * allAmount[1]) / allAmount[0]
                    }else if(allAmount.size == 1){
                        // When the two adapter has the same currency symbol
                        rateOfSecondCurrency = (1f * allAmount[0]) / allAmount[0]
                    }else{
                        rateOfSecondCurrency = 0f
                    }
                    Log.d("Currency", rateOfSecondCurrency.toString())
                }

            }

        })

        return rateOfSecondCurrency
    }


    fun fetchCurrency(): LiveData<List<Currency>> {
        val currencySymbols = StringBuilder()
        for(currency in Countries){
            currencySymbols.append(currency.currency)
            currencySymbols.append(",")
        }

        val theSymbol = currencySymbols.toString()

        Log.d("Symbol", theSymbol)

        val call: Call<JsonObject> = mApiService!!.getCurrencyRate("07dd79ddc7f86314dd8f391e87fdf958", theSymbol)

        call.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("ResponseStatus", "Failed: " + t.message)
                currencyLiveData.postValue(null)
                t.printStackTrace()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (!response.isSuccessful){
                    Log.d("ResponseStatus", "Unsuccessful: ${response.code()}" + response.body())
                    if (response.message() == "null"){
                        return
                    }else{
                        Log.d("ResponseMessage", "${response.message()}")
                    }
                    currencyLiveData.postValue(null)
                }

                if (response.isSuccessful){

                    Log.d("ResponseStatus", "successful: ${response.code()} " + response.body())
                    val getting = response.body()
                    val data = getting!!.get("rates").asJsonObject

                    val countryCurrencies = ArrayList<Currency>()

                    //Add the Euro Currency since it is not in the rates
                    val firstCurrency = Currency()
                    firstCurrency.id = 1
                    firstCurrency.currency = "EUR"
                    firstCurrency.currentValue = 1f

                    countryCurrencies.add(firstCurrency)

                    var count = 2
                    for(dataKey in data.keySet()){
                        val countryCurrency = Currency()
                        countryCurrency.id = count
                        countryCurrency.currency = dataKey
                        countryCurrency.currentValue = data.get(dataKey).asFloat

                        countryCurrencies.add(countryCurrency)
                        count++
                    }

                    currencyLiveData.postValue(countryCurrencies)

                }

            }
        })

        return currencyLiveData
    }


    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)


    //Repository Section For Country Currency Table Of the Currency Database

    //private val currencyDaos = initializeDatabse()

    private fun initializeDatabse(application: Application): CountryCurrencyDao {
        return CurrencyDatabase.getDatabase(application).conutryCurrencyDao()
    }


    fun insertCurrencyData(data: Currency, application: Application){
        scope.launch(Dispatchers.IO)  {
            initializeDatabse(application).insertData(data)
        }
    }

    fun getAllCurrencyData(application: Application): LiveData<List<Currency>> {
        val currencyData = initializeDatabse(application).getAllProducts()
        return currencyData
    }

    fun getRate(currency: String, application: Application): LiveData<Currency>{
        val currencyData = initializeDatabse(application).selectRatesByName(currency)
        return currencyData
    }

    fun deleteCurrencyData(application: Application){
        scope.launch(Dispatchers.IO)  {
            initializeDatabse(application).deleteData()
        }
    }


}