package com.example.findingcity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CityViewModel : ViewModel() {
    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> get() = _cities

    fun setCities(cityList: List<City>) {
        _cities.value = cityList
    }

    fun filterCities(query: String) {
        val filteredList = _cities.value?.filter {
            it.name.startsWith(query, ignoreCase = true)
        }
        _cities.value = filteredList
    }
}
