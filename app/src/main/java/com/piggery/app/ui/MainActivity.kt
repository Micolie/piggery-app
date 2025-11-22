package com.piggery.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.piggery.app.R
import com.piggery.app.databinding.ActivityMainBinding
import com.piggery.app.ui.pig.list.PigAdapter
import com.piggery.app.ui.pig.register.RegisterPigActivity
import com.piggery.app.viewmodel.PigViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PigViewModel by viewModels()
    private lateinit var adapter: PigAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
        setupFab()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.pig_inventory)
    }

    private fun setupRecyclerView() {
        adapter = PigAdapter { pig ->
            val intent = Intent(this, RegisterPigActivity::class.java).apply {
                putExtra(RegisterPigActivity.EXTRA_PIG_ID, pig.id)
            }
            startActivity(intent)
        }

        binding.pigsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSearchView() {
        binding.searchEditText.addTextChangedListener { text ->
            val query = text.toString().trim()
            if (query.isEmpty()) {
                viewModel.allPigs.observe(this) { pigs ->
                    updatePigList(pigs)
                }
            } else {
                viewModel.searchPigs(query).observe(this) { pigs ->
                    updatePigList(pigs)
                }
            }
        }
    }

    private fun setupFab() {
        binding.fabAddPig.setOnClickListener {
            startActivity(Intent(this, RegisterPigActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.allPigs.observe(this) { pigs ->
            updatePigList(pigs)
        }
    }

    private fun updatePigList(pigs: List<com.piggery.app.data.entity.Pig>) {
        adapter.submitList(pigs)

        if (pigs.isEmpty()) {
            binding.emptyView.visibility = View.VISIBLE
            binding.pigsRecyclerView.visibility = View.GONE
        } else {
            binding.emptyView.visibility = View.GONE
            binding.pigsRecyclerView.visibility = View.VISIBLE
        }
    }
}
