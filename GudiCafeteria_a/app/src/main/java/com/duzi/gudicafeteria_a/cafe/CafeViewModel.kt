package com.duzi.gudicafeteria_a.cafe

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.duzi.gudicafeteria_a.data.Cafe

class CafeViewModel: ViewModel() {
    private val repo: CafeRepository = CafeRepository.getInstance()

    private val cafeList: LiveData<List<Cafe>> = repo.getCafeList()
    fun getCafeList() = cafeList

    private val cafeListPeriod: LiveData<List<Cafe>> = repo.getCafeListPeriod()
    fun getCafeListPeriod() = cafeListPeriod
}