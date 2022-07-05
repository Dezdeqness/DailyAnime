package com.dezdeqness.advancedrecycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

inline fun <reified ChildModel : MainModel, MainModel : Any, Binding : ViewBinding> adapterDelegateViewBinding(
    modelClass: Class<out MainModel>,
    noinline viewBinding: (layoutInflater: LayoutInflater, parent: ViewGroup) -> Binding,
    noinline block: AdapterDelegateViewBindingViewHolder<ChildModel, Binding>.() -> Unit
): BaseAdapterDelegate<MainModel> {

    return DslAdapterDelegate(
        modelClass = modelClass,
        binding = viewBinding,
        initializerBlock = block,
    )
}

@PublishedApi
internal class DslAdapterDelegate<ChildModel : MainModel, MainModel : Any, Binding : ViewBinding>(
    override val modelClass: Class<out MainModel>,
    private val binding: (layoutInflater: LayoutInflater, parent: ViewGroup) -> Binding,
    private val initializerBlock: AdapterDelegateViewBindingViewHolder<ChildModel, Binding>.() -> Unit,
) : CastAdapterDelegate<
        ChildModel, MainModel, AdapterDelegateViewBindingViewHolder<ChildModel, Binding>>(modelClass) {

    override fun onCreateViewHolder(parent: ViewGroup) =
        AdapterDelegateViewBindingViewHolder<ChildModel, Binding>(
            binding(LayoutInflater.from(parent.context), parent)
        ).also {
            initializerBlock(it)
        }

    override fun onBindViewHolder(
        item: ChildModel,
        viewHolder: AdapterDelegateViewBindingViewHolder<ChildModel, Binding>,
        payloads: List<Any>
    ) {
        viewHolder.shadowItem = item
        viewHolder.bind?.invoke(payloads)
    }

}

class AdapterDelegateViewBindingViewHolder<T, Binding : ViewBinding>(
    val binding: Binding, view: View = binding.root
) : RecyclerView.ViewHolder(view) {

    private object Uninitialized

    internal var shadowItem: Any = Uninitialized
    val item: T
        get() = shadowItem as T

    internal var bind: ((payloads: List<Any>) -> Unit)? = null

    fun bind(bindingBlock: (payloads: List<Any>) -> Unit) {
        bind = bindingBlock
    }

    fun userInteraction(userInteraction: (item: T) -> Unit) {
        userInteraction(item)
    }

}
