package com.cookey.emmanuel.currencyconverter.persistence

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country_currency")
class CountryCurrency() {

    @PrimaryKey
    var id: Int?=null
    var currency: String=""
    var dollarValue: Double? = null
}