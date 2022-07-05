package com.dezdeqness.presentation

import com.dezdeqness.advancedrecycler.adapterDelegateViewBinding
import com.dezdeqness.presentation.models.AdapterItem
import com.dezdeqness.databinding.TestLayoutBinding
import com.dezdeqness.presentation.models.Test

fun dslAdapterDelegate() =
    adapterDelegateViewBinding<Test, AdapterItem, TestLayoutBinding>(
        modelClass = Test.javaClass,
        viewBinding = { layoutInflater, parent ->
            TestLayoutBinding.inflate(layoutInflater, parent, false)
        },
        block = {

            bind {

            }
        }
    )
