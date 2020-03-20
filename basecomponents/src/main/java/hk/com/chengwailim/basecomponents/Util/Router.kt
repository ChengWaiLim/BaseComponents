package hk.com.chengwailim.basecomponents.Util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

abstract class Router : AppCompatActivity() {
    protected fun goToPage(activity: Activity) {
        prepareIntent(activity)
    }

    protected fun goToPage(activity: Activity, intentCallback: (Intent) -> Intent) {
        prepareIntent(activity, { intentCallback.invoke(it) })
    }

    private fun prepareIntent(activity: Activity, callback: (Intent) -> Intent = { it }) {
        var intent = Intent()
        intent = setPage(activity, intent)
        intent = callback.invoke(intent)
        startActivity(intent)
    }

    protected abstract fun setPage(activity: Activity, intent: Intent): Intent

    interface Activity {}
}