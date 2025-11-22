package com.piggery.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.piggery.app.data.entity.Pig

@Dao
interface PigDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(pig: Pig): Long

    @Update
    suspend fun update(pig: Pig)

    @Delete
    suspend fun delete(pig: Pig)

    @Query("SELECT * FROM pigs WHERE id = :pigId")
    suspend fun getPigById(pigId: Long): Pig?

    @Query("SELECT * FROM pigs WHERE id = :pigId")
    fun getPigByIdLive(pigId: Long): LiveData<Pig?>

    @Query("SELECT * FROM pigs ORDER BY registeredDate DESC")
    fun getAllPigs(): LiveData<List<Pig>>

    @Query("SELECT * FROM pigs WHERE status = :status ORDER BY registeredDate DESC")
    fun getPigsByStatus(status: Pig.Status): LiveData<List<Pig>>

    @Query("SELECT * FROM pigs WHERE tagNumber = :tagNumber LIMIT 1")
    suspend fun getPigByTagNumber(tagNumber: String): Pig?

    @Query("SELECT * FROM pigs WHERE tagNumber LIKE '%' || :query || '%' OR breed LIKE '%' || :query || '%' ORDER BY registeredDate DESC")
    fun searchPigs(query: String): LiveData<List<Pig>>

    @Query("SELECT COUNT(*) FROM pigs")
    suspend fun getPigCount(): Int

    @Query("SELECT COUNT(*) FROM pigs WHERE status = :status")
    suspend fun getPigCountByStatus(status: Pig.Status): Int

    @Query("DELETE FROM pigs")
    suspend fun deleteAll()
}
