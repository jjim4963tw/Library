package com.jjim4963tw.library.layout.library.jetpack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jjim4963tw.library.databinding.AdapterViewBindingBinding

class ViewBindingAdapter : RecyclerView.Adapter<ViewBindingAdapter.ViewHolder>() {

    class ViewHolder(private val binding: AdapterViewBindingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String) {
            binding.tvTest1.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(AdapterViewBindingBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // ...
    }

    override fun getItemCount(): Int {
        return 0
    }
}