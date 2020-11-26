package com.cookey.emmanuel.currencyconverter

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineScatterCandleRadarDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer


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
        mChart.setGridBackgroundColor(Color.parseColor("#012CEA"))
        mChart.setDrawGridBackground(true)


        mChart.setDrawBorders(false)
        mChart.description.isEnabled = false

        mChart.setPinchZoom(false)

        val legend = mChart.legend
        legend.isEnabled = false

        setData(10, 50f)

    }

    private fun setData( count: Int, range: Float){

        val yVals = ArrayList<Entry>()
        yVals.add(Entry(0.toFloat(), 10f))
        yVals.add(Entry(2f, 1f))
        yVals.add(Entry(4f, 2f))
        yVals.add(Entry(8f, 3f))
        yVals.add(Entry(12f, 4f))
        yVals.add(Entry(15f, 5f))




        val set1 = LineDataSet(yVals, "Data Set1")
        set1.setColor(Color.TRANSPARENT)
        set1.setDrawCircles(false)
        set1.lineWidth = 3f
        set1.fillAlpha = 255
        set1.setDrawFilled(true)
        set1.fillColor = Color.parseColor("#185DFA")


        val data = LineData(set1)
        data.setDrawValues(false)


        mChart.setData(data)

    }


}

