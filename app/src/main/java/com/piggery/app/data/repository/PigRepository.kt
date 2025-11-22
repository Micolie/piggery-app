package com.piggery.app.data.repository

import androidx.lifecycle.LiveData
import com.piggery.app.data.dao.PigDao
import com.piggery.app.data.entity.Pig

class PigRepository(private val pigDao: PigDao) {

    val allPigs: LiveData<List<Pig>> = pigDao.getAllPigs()

    suspend fun insert(pig: Pig): Long {
        return pigDao.insert(pig)
    }

    suspend fun update(pig: Pig) {
        pigDao.update(pig)
    }

    suspend fun delete(pig: Pig) {
        pigDao.delete(pig)
    }

    suspend fun getPigById(pigId: Long): Pig? {
        return pigDao.getPigById(pigId)
    }

    fun getPigByIdLive(pigId: Long): LiveData<Pig?> {
        return pigDao.getPigByIdLive(pigId)
    }

    fun getPigsByStatus(status: Pig.Status): LiveData<List<Pig>> {
        return pigDao.getPigsByStatus(status)
    }

    suspend fun getPigByTagNumber(tagNumber: String): Pig? {
        return pigDao.getPigByTagNumber(tagNumber)
    }

    fun searchPigs(query: String): LiveData<List<Pig>> {
        return pigDao.searchPigs(query)
    }

    suspend fun getPigCount(): Int {
        return pigDao.getPigCount()
    }

    suspend fun getPigCountByStatus(status: Pig.Status): Int {
        return pigDao.getPigCountByStatus(status)
    }
}
