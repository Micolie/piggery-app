package com.piggery.app.ui.pig.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.piggery.app.R
import com.piggery.app.data.entity.Pig
import com.piggery.app.databinding.ItemPigBinding

class PigAdapter(
    private val onPigClick: (Pig) -> Unit
) : ListAdapter<Pig, PigAdapter.PigViewHolder>(PigDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PigViewHolder {
        val binding = ItemPigBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PigViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PigViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PigViewHolder(
        private val binding: ItemPigBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPigClick(getItem(position))
                }
            }
        }

        fun bind(pig: Pig) {
            binding.apply {
                tagNumberTextView.text = "Tag #${pig.tagNumber}"
                breedTextView.text = pig.breed
                genderTextView.text = pig.gender.name
                ageTextView.text = root.context.getString(R.string.days_old, pig.getAgeInDays())
                weightTextView.text = "${pig.weight} ${root.context.getString(R.string.kg)}"
                statusTextView.text = pig.status.name
                statusTextView.setBackgroundColor(getStatusColor(pig.status))

                // Load photo if available
                if (pig.photoPath != null) {
                    Glide.with(root.context)
                        .load(pig.photoPath)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_gallery)
                        .centerCrop()
                        .into(pigImageView)
                } else {
                    pigImageView.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            }
        }

        private fun getStatusColor(status: Pig.Status): Int {
            val context = binding.root.context
            return when (status) {
                Pig.Status.ACTIVE -> context.getColor(R.color.status_active)
                Pig.Status.SOLD -> context.getColor(R.color.status_sold)
                Pig.Status.DECEASED -> context.getColor(R.color.status_deceased)
                Pig.Status.QUARANTINE -> context.getColor(R.color.status_quarantine)
            }
        }
    }

    private class PigDiffCallback : DiffUtil.ItemCallback<Pig>() {
        override fun areItemsTheSame(oldItem: Pig, newItem: Pig): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pig, newItem: Pig): Boolean {
            return oldItem == newItem
        }
    }
}
