package com.cookey.emmanuel.currencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cookey.emmanuel.currencyconverter.persistence.CountryCurrency
import com.cookey.emmanuel.currencyconverter.repository.CurrencyRepository

class CurrencyViewModel constructor(application: Application): AndroidViewModel(application) {

    private val repository: CurrencyRepository = CurrencyRepository(application)


    fun insertResponse(response: CountryCurrency){
        repository.insertCurrencyData(response)
    }

    fun getAllCurrencyRate(): LiveData<List<CountryCurrency>> {
        return repository.getAllCurrencyData()
    }

    fun deleteCurrencyResponse(){
        repository.deleteCurrencyData()
    }

}