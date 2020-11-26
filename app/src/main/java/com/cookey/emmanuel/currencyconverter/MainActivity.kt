package com.cookey.emmanuel.currencyconverter

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class MainActivity : AppCompatActivity() {

    lateinit var mChart: LineChart
    lateinit var firstSpinner: Spinner
    lateinit var secondSpinner: Spinner
    lateinit var convertBtn: Button
    lateinit var firstEditText: EditText
    lateinit var seconEditText: EditText
    lateinit var firstHintCurrencyText: TextView
    lateinit var secondHintCurrencyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        mChart = findViewById(R.id.chart)
        firstSpinner = findViewById(R.id.spinner_one)
        secondSpinner = findViewById(R.id.spinner_two)
        convertBtn = findViewById(R.id.convert_btn)
        firstEditText = findViewById(R.id.first_user_input)
        seconEditText = findViewById(R.id.second_user_input)
        firstHintCurrencyText = findViewById(R.id.first_txt_hint)
        secondHintCurrencyText = findViewById(R.id.second_user_input)

        spinnerSetup()


        // setting up chart attributes
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


    private  fun spinnerSetup(){

        // Setup For the first Spinner/Drop Down
        val firstCurrencyData = ArrayList<String>()
        firstCurrencyData.add("PLN")
        val adapterOne = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, firstCurrencyData)
        adapterOne.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        firstSpinner.adapter = adapterOne

        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedText = p0?.getChildAt(0) as? TextView
                selectedText?.setTextColor(Color.parseColor("#084482"))
                firstHintCurrencyText.text = p0?.getItemAtPosition(p2).toString()
            }

        }


        // Setup For the second Spinner/Drop Down
        val secondCurrencyData = ArrayList<String>()
        secondCurrencyData.add("USD")
        val adapterTwo = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, secondCurrencyData)
        adapterTwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondSpinner.adapter = adapterTwo

        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedText = p0?.getChildAt(0) as? TextView
                selectedText?.setTextColor(Color.parseColor("#084482"))
                secondHintCurrencyText.text = p0?.getItemAtPosition(p2).toString()
            }

        }
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

