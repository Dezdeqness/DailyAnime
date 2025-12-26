package com.dezdeqness.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.Multibinds
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

class ViewModelFactory @Inject constructor(
    private val creators: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>,
    private val assistedCreators: Map<Class<out ViewModel>, AssistedViewModelFactory<out ViewModel>>,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        var assisted: AssistedViewModelFactory<out ViewModel>? = assistedCreators[modelClass]
        if (assisted == null) {
            for ((key, value) in assistedCreators) {
                if (modelClass.isAssignableFrom(key)) {
                    assisted = value
                    break
                }
            }
        }
        if (assisted != null) {
            @Suppress("UNCHECKED_CAST")
            return assisted.create(extras) as T
        }

        var creator: Provider<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }
        require(creator != null) { "Unknown model class: $modelClass" }

        @Suppress("UNCHECKED_CAST")
        return creator.get() as T
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return create(modelClass, CreationExtras.Empty)
    }
}

interface AssistedViewModelFactory<out T : ViewModel> {
    fun create(extras: CreationExtras): T
}

@Module
abstract class ViewModelBuilderModule {

    @Multibinds
    abstract fun viewModelCreators(): Map<Class<out ViewModel>, ViewModel>

    @Multibinds
    abstract fun assistedViewModelCreators(): Map<Class<out ViewModel>, AssistedViewModelFactory<out ViewModel>>

    @Binds
    abstract fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory
}

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Target(
    AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class AssistedViewModelKey(val value: KClass<out ViewModel>)
