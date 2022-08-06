package com.dezdeqness

import android.app.Application
import com.dezdeqness.di.AppComponent
import com.dezdeqness.di.DaggerAppComponent

class ShikimoriApp : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}

fun Application.getComponent(): AppComponent {
    return (this as ShikimoriApp).appComponent
}
