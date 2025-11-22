package com.piggery.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.piggery.app.data.database.PiggeryDatabase
import com.piggery.app.data.entity.Pig
import com.piggery.app.data.repository.PigRepository
import kotlinx.coroutines.launch

class PigViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PigRepository
    val allPigs: LiveData<List<Pig>>

    init {
        val pigDao = PiggeryDatabase.getDatabase(application).pigDao()
        repository = PigRepository(pigDao)
        allPigs = repository.allPigs
    }

    fun insert(pig: Pig, onSuccess: (Long) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Check if tag number already exists
                val existing = repository.getPigByTagNumber(pig.tagNumber)
                if (existing != null) {
                    onError("Tag number already exists")
                    return@launch
                }

                val id = repository.insert(pig)
                onSuccess(id)
            } catch (e: Exception) {
                onError(e.message ?: "Error saving pig")
            }
        }
    }

    fun update(pig: Pig, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                // Check if tag number exists for a different pig
                val existing = repository.getPigByTagNumber(pig.tagNumber)
                if (existing != null && existing.id != pig.id) {
                    onError("Tag number already exists")
                    return@launch
                }

                repository.update(pig.copy(lastUpdated = System.currentTimeMillis()))
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error updating pig")
            }
        }
    }

    fun delete(pig: Pig, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                repository.delete(pig)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error deleting pig")
            }
        }
    }

    fun getPigById(pigId: Long): LiveData<Pig?> {
        return repository.getPigByIdLive(pigId)
    }

    fun getPigsByStatus(status: Pig.Status): LiveData<List<Pig>> {
        return repository.getPigsByStatus(status)
    }

    fun searchPigs(query: String): LiveData<List<Pig>> {
        return repository.searchPigs(query)
    }
}
