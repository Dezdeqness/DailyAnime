package com.dezdeqness.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dezdeqness.advancedrecycler.CastAdapterDelegate
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.databinding.TestLayoutBinding
import com.dezdeqness.presentation.models.Test

class TestBaseAdapterDelegate :
    CastAdapterDelegate<Test, AdapterItem, TestBaseAdapterDelegate.TestViewHolder>(
        Test.javaClass
    ) {

    override fun onCreateViewHolder(parent: ViewGroup) =
        TestViewHolder(
            TestLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(item: Test, viewHolder: TestViewHolder, payloads: List<Any>) {
        with(viewHolder) {
            binding.root.setOnClickListener {

            }
        }
    }

    class TestViewHolder(val binding: TestLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}

