package com.cookey.emmanuel.currencyconverter.persistence

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "country_currency", indices = arrayOf(Index(value = ["currency"], unique = true)))
class Currency() {

    @PrimaryKey
    var id: Int?=null
    var currency: String=""
    var currentValue: Float? = null
}