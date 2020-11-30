package com.cookey.emmanuel.currencyconverter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cookey.emmanuel.currencyconverter.persistence.Currency
import com.cookey.emmanuel.currencyconverter.repository.CurrencyRepository


class CurrencyViewModel constructor(application: Application): AndroidViewModel(application) {

    val app = application

    private val repository: CurrencyRepository = CurrencyRepository.instance

    private val countryCurrency: LiveData<List<Currency>> = repository.fetchCurrency()

    fun currencyDataBydate(time: String, involvedCurrency: String): Float {
        return repository.fetchCurrencyByDate(time, involvedCurrency)
    }

    fun getUser(): LiveData<List<Currency>>? {
        return countryCurrency
    }


    fun insertResponse(response: Currency) {
        repository.insertCurrencyData(response, app)
    }

    fun getParticularRate(currency: String): LiveData<Currency> {
        return repository.getRate(currency, app)
    }

    fun deleteCurrencyResponse(){
        repository.deleteCurrencyData(app)
    }

}