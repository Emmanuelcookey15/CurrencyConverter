package com.cookey.emmanuel.currencyconverter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.cookey.emmanuel.currencyconverter.persistence.Currency
import de.hdodenhof.circleimageview.CircleImageView

class MySpinnerAdapter(private val ctx: Context, arrays: ArrayList<Country>): ArrayAdapter<Country>(ctx, 0, arrays) {

    private var contentArray= arrays


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.my_spinner_layout, null)


        val textView = row.findViewById(R.id.spinnerTextView) as TextView
        textView.text = contentArray[position].currency


        val imageView = row.findViewById(R.id.users_bank_img) as CircleImageView

        imageView.setImageResource(contentArray[position].flag)

        return row
    }



}