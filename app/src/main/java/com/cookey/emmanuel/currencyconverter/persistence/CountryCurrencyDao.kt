package com.cookey.emmanuel.currencyconverter.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(currencyVal: Currency)

    @Query("SELECT * FROM country_currency WHERE currency LIKE :currency ORDER BY currency")
    fun selectRatesByName(currency:String): LiveData<Currency>

    @Query("UPDATE country_currency set currentValue=:currentValue where id=:id")
    fun updateRates(currentValue: Float, id: Int)

    // /** get all product **/
    @Query("SELECT * FROM  country_currency")
    fun getAllProducts(): LiveData<List<Currency>>

    @Query("SELECT COUNT(*) from country_currency")
    fun ratesCount() : Int


    @Query("DELETE FROM country_currency")
    fun deleteData()

}