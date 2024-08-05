package com.example.findingcity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var cityViewModel: CityViewModel
    private lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cityViewModel.cities.observe(this, Observer { cities ->
            cityAdapter = CityAdapter(cities)
            recyclerView.adapter = cityAdapter
        })

        val searchEditText: EditText = findViewById(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cityViewModel.filterCities(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Load cities from JSON file and set to ViewModel
        val json = readJsonFromAssets(this, "cities-1.json")
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, City::class.java)
        val adapter: JsonAdapter<List<City>> = moshi.adapter(listType)
        val cities: List<City>? = adapter.fromJson(json)

        cities?.let {
            cityViewModel.setCities(it)
        }
    }
    fun readJsonFromAssets(context: Context, fileName: String): String {
        val json: String
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return ""
        }
        return json
    }
}

