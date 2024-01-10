
import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author guoqingshan
 * @date 2023/11/24/024
 * @description
 */
object ActivityManager {
    private var application: Application? = null
    private var currentTopActivity: AppCompatActivity? = null

    fun init(application: Application) {
        this.application = application
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                currentTopActivity = activity as? AppCompatActivity
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

    fun getCurrentActivity(): AppCompatActivity = currentTopActivity!!

    fun getApp(): Application = application!!
}