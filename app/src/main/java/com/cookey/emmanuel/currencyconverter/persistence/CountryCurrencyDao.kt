package com.cookey.emmanuel.currencyconverter.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryCurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(currencyVal: CountryCurrency)


    // /** get all product **/
    @Query("SELECT * FROM  country_currency")
    fun getAllProducts(): LiveData<List<CountryCurrency>>


    @Query("DELETE FROM country_currency")
    fun deleteData()

}