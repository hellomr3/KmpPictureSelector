
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author guoqingshan
 * @date 2023/11/24/024
 * @description
 */
object ActivityManager {
    private val application:MutableStateFlow<Application?> = MutableStateFlow(null)
    private val currentTopActivity:MutableStateFlow<ComponentActivity?> = MutableStateFlow(null)

    fun init(application: Application) {
        this.application.value = application
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                currentTopActivity.value = activity as? ComponentActivity
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {

            }

        })
    }

    fun getCurrentActivity(): ComponentActivity= currentTopActivity.value!!

    fun getApp(): Application = application.value!!
}