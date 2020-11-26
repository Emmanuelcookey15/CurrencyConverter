package com.cookey.emmanuel.currencyconverter.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.cookey.emmanuel.currencyconverter.persistence.CountryCurrency
import com.cookey.emmanuel.currencyconverter.persistence.CurrencyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CurrencyRepository(application: Application) {

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)


    //Repository Section For Country Currency Table Of the Currency Dataase
    private val currencyDaos = CurrencyDatabase.getDatabase(application).conutryCurrencyDao()


    fun insertCurrencyData(data: CountryCurrency){
        scope.launch(Dispatchers.IO)  {
            currencyDaos.insertData(data)
        }
    }

    fun getAllCurrencyData(): LiveData<List<CountryCurrency>> {
        val currencyData = currencyDaos.getAllProducts()
        return currencyData
    }

    fun deleteCurrencyData(){
        scope.launch(Dispatchers.IO)  {
            currencyDaos.deleteData()
        }
    }




}