package com.example.mesenger

import android.app.Activity
import android.app.Application
import android.os.Bundle

class MYApplicatoinLifeCycleCallback: Application.ActivityLifecycleCallbacks {

      private var activityReferences = 0
      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            TODO("Not yet implemented")
      }

      override fun onActivityStarted(activity: Activity) {
                activityReferences++

      }

      override fun onActivityResumed(activity: Activity) {
            TODO("Not yet implemented")
      }

      override fun onActivityPaused(activity: Activity) {

            TODO("Not yet implemented")
      }

      override fun onActivityStopped(activity: Activity) {
                activityReferences--

      }

      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            TODO("Not yet implemented")
      }

      override fun onActivityDestroyed(activity: Activity) {
            TODO("Not yet implemented")
      }

      fun isApplicationInForeground(): Boolean {
            return activityReferences > 0
      }

}