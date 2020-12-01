package com.cookey.emmanuel.currencyconverter

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cookey.emmanuel.currencyconverter.api.ApiService
import com.cookey.emmanuel.currencyconverter.viewmodel.CurrencyViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.android.synthetic.main.activity_main.*
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var adapterOne: MySpinnerAdapter? = null
    private var adapterTwo: MySpinnerAdapter? = null
    lateinit var mChart: LineChart
    lateinit var firstSpinner: Spinner
    lateinit var secondSpinner: Spinner
    lateinit var convertBtn: Button
    lateinit var firstEditText: EditText
    lateinit var seconEditText: EditText
    lateinit var firstHintCurrencyText: TextView
    lateinit var secondHintCurrencyText: TextView

    val x = ArrayList<Entry>()

    val dt = Date()
    val dtOrg = DateTime(dt)
    val dtPlus6: DateTime = dtOrg.minusDays(6)
    val dtPlus12: DateTime = dtOrg.minusDays(12)
    val dtPlus24: DateTime = dtOrg.minusDays(24)
    val dtPlus28: DateTime = dtOrg.minusDays(28)

    var firstTextField = ""
    var secondTextField = ""

    var firstTextFieldIsClicked = false
    var secondTextFieldIsClicked = false

    var mApiService: ApiService? = null

    lateinit var viewmodel: CurrencyViewModel

    var firstDataBaseCurrencyValue = 0f
    var secondDataBaseCurrencyValue = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }


        viewmodel = ViewModelProvider(this).get(CurrencyViewModel::class.java)


        mChart = findViewById(R.id.chart)
        firstSpinner = findViewById(R.id.spinner_one)
        secondSpinner = findViewById(R.id.spinner_two)
        convertBtn = findViewById(R.id.convert_btn)
        firstEditText = findViewById(R.id.first_user_input)
        seconEditText = findViewById(R.id.second_user_input)
        firstHintCurrencyText = findViewById(R.id.first_txt_hint)
        secondHintCurrencyText = findViewById(R.id.second_txt_hint)

        spinnerSetup()

        saveFetchedData()

        // setting up chart attributes
        mChart.setBackgroundColor(Color.TRANSPARENT)
        mChart.setGridBackgroundColor(Color.parseColor("#0075FF"))
        mChart.setDrawGridBackground(true)

        mChart.setDrawBorders(false)
        mChart.description.isEnabled = false

        mChart.setPinchZoom(false)
        val xl = mChart.xAxis
        xl.setAvoidFirstLastClipping(true)
        val leftAxis = mChart.axisLeft
        val rightAxis = mChart.axisRight
        rightAxis.isEnabled = false
        val l = mChart.legend
        l.form = Legend.LegendForm.LINE
        l.isEnabled = false

        convertBtn.setOnClickListener {
            firstEditText.setText(firstTextField, TextView.BufferType.EDITABLE)
            seconEditText.setText(secondTextField, TextView.BufferType.EDITABLE)
            setChartData()
        }



        firstEditText.setOnTouchListener { v, event ->

            firstTextFieldIsClicked = true
            secondTextFieldIsClicked = false

            return@setOnTouchListener false
        }

        seconEditText.setOnTouchListener { v, event ->
            secondTextFieldIsClicked = true
            firstTextFieldIsClicked = false

            return@setOnTouchListener false
        }

        firstEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty() && firstTextFieldIsClicked) {
                    firstTextField = p0.toString()


                    try {
                        var amountFirst = p0.toString().toFloat()
                        viewmodel.getParticularRate(firstHintCurrencyText.text.toString())
                            .observe(this@MainActivity, Observer {
                                firstDataBaseCurrencyValue = it.currentValue!!
                                viewmodel.getParticularRate(secondHintCurrencyText.text.toString())
                                    .observe(this@MainActivity, Observer {
                                        secondDataBaseCurrencyValue = it.currentValue!!
                                        var amountSecond =
                                            (amountFirst * secondDataBaseCurrencyValue) / firstDataBaseCurrencyValue

                                        secondTextField = amountSecond.toString()
                                        Log.d("WHYNAN", secondTextField)
                                        Log.d("WHYNAN", amountFirst.toString())
                                        Log.d("WHYNAN", secondDataBaseCurrencyValue.toString())
                                        Log.d("WHYNAN", firstDataBaseCurrencyValue.toString())

                                        seconEditText.setText(secondTextField, TextView.BufferType.EDITABLE)
                                    })
                            })
                    } catch(ex: NumberFormatException){ // handle your exception
                        ex.printStackTrace()
                    }

                }

            }

        })

        seconEditText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty() && secondTextFieldIsClicked) {
                    secondTextField = p0.toString()

                    try {
                        var amountSecond = p0.toString().toFloat()
                        viewmodel.getParticularRate(firstHintCurrencyText.text.toString())
                            .observe(this@MainActivity, Observer {
                                firstDataBaseCurrencyValue = it.currentValue!!
                                viewmodel.getParticularRate(secondHintCurrencyText.text.toString())
                                    .observe(this@MainActivity, Observer {
                                        secondDataBaseCurrencyValue = it.currentValue!!
                                        var amountFirst =
                                            (amountSecond * firstDataBaseCurrencyValue) / secondDataBaseCurrencyValue

                                        firstTextField = amountFirst.toString()

                                        firstEditText.setText(
                                            firstTextField,
                                            TextView.BufferType.EDITABLE
                                        )
                                    })
                            })
                    } catch(ex: NumberFormatException){ // handle your exception
                        ex.printStackTrace()
                    }

                }
            }

        })
    }


    fun saveFetchedData() {

        viewmodel.getUser()?.observe(this, Observer { list ->
            list.forEach {
                viewmodel.insertResponse(it)
                Log.d("INSERTED", it.currency)
            }
        })

    }




    private fun spinnerSetup() {

        // Setup For the first Spinner/Drop Down
        adapterOne = MySpinnerAdapter(this, Countries)
        firstSpinner.adapter = adapterOne

        firstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedText = Countries[p2].currency as? TextView
                selectedText?.setTextColor(Color.parseColor("#084482"))
                firstHintCurrencyText.text = Countries[p2].currency

            }

        }

        // Setup For the second Spinner/Drop Down
        adapterTwo = MySpinnerAdapter(this, Countries)
        secondSpinner.adapter = adapterTwo

        secondSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedText = Countries[p2].currency as? TextView
                selectedText?.setTextColor(Color.parseColor("#084482"))
                secondHintCurrencyText.text = Countries[p2].currency
            }

        }
    }



    private fun ratesByDay(time: DateTime): String{

        val dateFormat = SimpleDateFormat("YYYY-MM-dd",  Locale.getDefault())
        val day = dateFormat.format(time.toCalendar(Locale.getDefault()).time)
        Log.d("HISTORYDATA", "$day")
        return day
    }


    private fun setChartData() {

        val currencyInvolved = "${firstHintCurrencyText.text.toString()},${secondHintCurrencyText.text.toString()}"

        val dateFormatTextview = SimpleDateFormat("dd MMM", Locale.getDefault())

        var floatOne = 0f
        var floatTwo = 0f
        var floatThree = 0f
        var floatFour = 0f
        var floatFive = 0f

        chart_txt_one.text = dateFormatTextview.format(dtOrg.toCalendar(Locale.getDefault()).time)
        x.add(Entry(viewmodel.currencyDataBydate(ratesByDay(dtOrg), currencyInvolved), 1f))



        chart_txt_two.text = dateFormatTextview.format(dtPlus6.toCalendar(Locale.getDefault()).time)
        x.add(Entry( viewmodel.currencyDataBydate(ratesByDay(dtPlus6), currencyInvolved), 6f))


        chart_txt_three.text = dateFormatTextview.format(dtPlus12.toCalendar(Locale.getDefault()).time)
        x.add(Entry( viewmodel.currencyDataBydate(ratesByDay(dtPlus12), currencyInvolved), 12f))


        chart_txt_four.text = dateFormatTextview.format(dtPlus24.toCalendar(Locale.getDefault()).time)
        x.add(Entry( viewmodel.currencyDataBydate(ratesByDay(dtPlus24), currencyInvolved), 6f))


        chart_txt_five.text = dateFormatTextview.format(dtPlus28.toCalendar(Locale.getDefault()).time)
        x.add(Entry( viewmodel.currencyDataBydate(ratesByDay(dtPlus28), currencyInvolved), 28f))

        val set1 = LineDataSet(x, "")
        set1.color = Color.TRANSPARENT
        set1.setDrawCircles(false)
        set1.lineWidth = 3f
        set1.fillAlpha = 255
        set1.setDrawFilled(true)
        set1.fillColor = Color.parseColor("#0168E3")

        val data = LineData(set1)
        data.setDrawValues(false)

        mChart.data = data
    }


}

