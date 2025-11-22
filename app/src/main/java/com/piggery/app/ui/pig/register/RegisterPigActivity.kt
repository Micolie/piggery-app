package com.piggery.app.ui.pig.register

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.piggery.app.R
import com.piggery.app.data.entity.Pig
import com.piggery.app.databinding.ActivityRegisterPigBinding
import com.piggery.app.util.DateUtil
import com.piggery.app.util.PhotoUtil
import com.piggery.app.viewmodel.PigViewModel
import java.util.*

class RegisterPigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterPigBinding
    private val viewModel: PigViewModel by viewModels()

    private var pigId: Long = -1L
    private var currentPig: Pig? = null
    private var selectedDateOfBirth: Long = System.currentTimeMillis()
    private var currentPhotoPath: String? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchCamera()
        } else {
            Toast.makeText(this, R.string.camera_permission_required, Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val savedPath = PhotoUtil.savePhoto(this, bitmap)
            if (savedPath != null) {
                // Delete old photo if exists
                if (currentPhotoPath != null && currentPhotoPath != savedPath) {
                    PhotoUtil.deletePhoto(currentPhotoPath)
                }
                currentPhotoPath = savedPath
                displayPhoto()
            } else {
                Toast.makeText(this, "Failed to save photo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pigId = intent.getLongExtra(EXTRA_PIG_ID, -1L)

        setupToolbar()
        setupStatusSpinner()
        setupDatePicker()
        setupPhotoButtons()
        setupSaveButton()

        if (pigId != -1L) {
            loadPig()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = if (pigId == -1L) {
                getString(R.string.register_pig)
            } else {
                getString(R.string.edit_pig)
            }
        }
    }

    private fun setupStatusSpinner() {
        val statuses = Pig.Status.values().map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.statusSpinner.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.dateOfBirthEditText.setOnClickListener {
            val calendar = DateUtil.getCalendar(selectedDateOfBirth)

            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }
                    selectedDateOfBirth = selectedCalendar.timeInMillis
                    binding.dateOfBirthEditText.setText(DateUtil.formatDate(selectedDateOfBirth))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Set initial date
        binding.dateOfBirthEditText.setText(DateUtil.formatDate(selectedDateOfBirth))
    }

    private fun setupPhotoButtons() {
        binding.takePhotoButton.setOnClickListener {
            checkCameraPermissionAndLaunch()
        }

        binding.removePhotoButton.setOnClickListener {
            if (currentPhotoPath != null) {
                PhotoUtil.deletePhoto(currentPhotoPath)
                currentPhotoPath = null
                binding.photoImageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {
            if (validateInput()) {
                savePig()
            }
        }
    }

    private fun checkCameraPermissionAndLaunch() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                launchCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun launchCamera() {
        takePictureLauncher.launch(null)
    }

    private fun displayPhoto() {
        val bitmap = PhotoUtil.loadBitmap(currentPhotoPath)
        if (bitmap != null) {
            Glide.with(this)
                .load(bitmap)
                .centerCrop()
                .into(binding.photoImageView)
        }
    }

    private fun loadPig() {
        viewModel.getPigById(pigId).observe(this) { pig ->
            pig?.let {
                currentPig = it
                populateFields(it)
            }
        }
    }

    private fun populateFields(pig: Pig) {
        binding.apply {
            tagNumberEditText.setText(pig.tagNumber)
            breedEditText.setText(pig.breed)

            when (pig.gender) {
                Pig.Gender.MALE -> maleRadioButton.isChecked = true
                Pig.Gender.FEMALE -> femaleRadioButton.isChecked = true
            }

            selectedDateOfBirth = pig.dateOfBirth
            dateOfBirthEditText.setText(DateUtil.formatDate(pig.dateOfBirth))

            weightEditText.setText(pig.weight.toString())

            statusSpinner.setSelection(pig.status.ordinal)

            notesEditText.setText(pig.notes ?: "")

            currentPhotoPath = pig.photoPath
            if (currentPhotoPath != null) {
                displayPhoto()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true

        val tagNumber = binding.tagNumberEditText.text.toString().trim()
        if (tagNumber.isEmpty()) {
            binding.tagNumberLayout.error = getString(R.string.error_tag_required)
            isValid = false
        } else {
            binding.tagNumberLayout.error = null
        }

        val breed = binding.breedEditText.text.toString().trim()
        if (breed.isEmpty()) {
            binding.breedLayout.error = getString(R.string.error_breed_required)
            isValid = false
        } else {
            binding.breedLayout.error = null
        }

        val weightText = binding.weightEditText.text.toString().trim()
        if (weightText.isEmpty() || weightText.toDoubleOrNull() == null || weightText.toDouble() <= 0) {
            binding.weightLayout.error = getString(R.string.error_invalid_weight)
            isValid = false
        } else {
            binding.weightLayout.error = null
        }

        return isValid
    }

    private fun savePig() {
        val tagNumber = binding.tagNumberEditText.text.toString().trim()
        val breed = binding.breedEditText.text.toString().trim()
        val gender = if (binding.maleRadioButton.isChecked) {
            Pig.Gender.MALE
        } else {
            Pig.Gender.FEMALE
        }
        val weight = binding.weightEditText.text.toString().toDouble()
        val status = Pig.Status.values()[binding.statusSpinner.selectedItemPosition]
        val notes = binding.notesEditText.text.toString().trim().ifEmpty { null }

        val pig = if (currentPig != null) {
            currentPig!!.copy(
                tagNumber = tagNumber,
                breed = breed,
                gender = gender,
                dateOfBirth = selectedDateOfBirth,
                weight = weight,
                status = status,
                photoPath = currentPhotoPath,
                notes = notes
            )
        } else {
            Pig(
                tagNumber = tagNumber,
                breed = breed,
                gender = gender,
                dateOfBirth = selectedDateOfBirth,
                weight = weight,
                status = status,
                photoPath = currentPhotoPath,
                notes = notes
            )
        }

        if (currentPig != null) {
            viewModel.update(
                pig,
                onSuccess = {
                    Toast.makeText(this, R.string.pig_updated, Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { error ->
                    if (error.contains("already exists")) {
                        binding.tagNumberLayout.error = getString(R.string.error_tag_exists)
                    } else {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        } else {
            viewModel.insert(
                pig,
                onSuccess = {
                    Toast.makeText(this, R.string.pig_saved, Toast.LENGTH_SHORT).show()
                    finish()
                },
                onError = { error ->
                    if (error.contains("already exists")) {
                        binding.tagNumberLayout.error = getString(R.string.error_tag_exists)
                    } else {
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    companion object {
        const val EXTRA_PIG_ID = "extra_pig_id"
    }
}
