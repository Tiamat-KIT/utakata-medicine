package com.example.utakata_medicine

import androidx.annotation.WorkerThread
import androidx.constraintlayout.helper.widget.Flow
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Flow

class MedicineRepository (private val medicineDao: MedicineDao){
    val allMedcines: Flow<List<MedicineClass>> = medicineDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(medicine: MedicineClass) {
        medicineDao.insertMedicine(medicine)
    }
}