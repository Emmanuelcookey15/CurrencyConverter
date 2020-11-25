package com.cookey.emmanuel.currencyconverter

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry

class MainActivity : AppCompatActivity() {

    lateinit var mChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        mChart = findViewById<LineChart>(R.id.chart)
        mChart.setBackgroundColor(Color.TRANSPARENT)
        mChart.setGridBackgroundColor(Color.WHITE)
        mChart.setDrawGridBackground(true)


        mChart.setDrawBorders(true)
        mChart.description.isEnabled = false
        mChart.setPinchZoom(false)

        val legend = mChart.legend
        legend.isEnabled = false


    }

    private fun setData(count: Int, range: Float){

        val yVals = ArrayList<Entry>()


    }


}
