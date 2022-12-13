package com.arzekeil.currency_converter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val API_KEY = ""
private const val BASE_URL = "https://v6.exchangerate-api.com"
private const val INITIAL_AMOUNT = 0.00

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerOriginCurrency: Spinner
    private lateinit var spinnerTargetCurrency: Spinner
    private lateinit var tvTargetCurrencyResult: TextView
    private lateinit var etOriginCurrencyValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currencies = resources.getStringArray(R.array.currencies)

        spinnerOriginCurrency = findViewById(R.id.spinner_origin_currency)
        spinnerTargetCurrency = findViewById(R.id.spinner_target_currency)
        tvTargetCurrencyResult = findViewById(R.id.tv_target_currency_result)
        etOriginCurrencyValue = findViewById(R.id.et_origin_currency_value)

        if (spinnerOriginCurrency != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                currencies
            )
            spinnerOriginCurrency.adapter = adapter

            spinnerOriginCurrency.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertCurrency()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
        if (spinnerTargetCurrency != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                currencies
            )
            spinnerTargetCurrency.adapter = adapter

            spinnerTargetCurrency.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertCurrency()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        etOriginCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                convertCurrency()
            }

        })

        tvTargetCurrencyResult.text = "%.2f".format(INITIAL_AMOUNT)
    }

    private fun convertCurrency() {
        if (etOriginCurrencyValue.text.isEmpty() and !spinnerOriginCurrency.isSelected and !spinnerTargetCurrency.isSelected) return

        val currencyApi = RetrofitHelper.getInstance(BASE_URL).create(CurrencyApi::class.java)

        GlobalScope.launch(Dispatchers.Main) {
            val result = currencyApi.getConversionRate(api_key = API_KEY, origin = spinnerOriginCurrency.selectedItem.toString(), target = spinnerTargetCurrency.selectedItem.toString(), amount = etOriginCurrencyValue.text.toString().toDouble())

            if (result != null) {
                Log.d("Result Body: ", result.body().toString())
                tvTargetCurrencyResult.text = result.body()!!.conversion_result.toString()
                Log.d("Conversion Results: ", result.body()!!.conversion_result.toString())
            }
        }
    }
}
