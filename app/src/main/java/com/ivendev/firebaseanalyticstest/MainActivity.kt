package com.ivendev.firebaseanalyticstest

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "testLogEvent"
        private const val EVENT_PUSH_CLICK = "push_click"
        private const val PARAM_PUSH_CLICK_URL = "url"
        private const val TEST_URL = "https://petrovich.ru/action-type/profit/?utm_source=handh&utm_medium=push&utm_campaign=handhTest"
    }

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firebaseAnalytics = FirebaseAnalytics.getInstance(application)
        buttonSendEvent.setOnClickListener {
            onPushClick(TEST_URL)
        }
    }

    /**
     * Send pushClick event to Firebase
     */
    private fun onPushClick(@Nullable clickUrl: String?) {
        clickUrl?.let { notNullUrl ->
            val bundle: Bundle? = createUtmBundle(notNullUrl)
            firebaseAnalytics.logEvent(EVENT_PUSH_CLICK, bundle)
            Log.d(TAG, "bundle - $bundle")
        }
    }

    /**
     * Return Bundle with params from url
     */
    private fun createUtmBundle(clickUrl: String): Bundle? {
        val bundle = Bundle()
        val uri: Uri = Uri.parse(clickUrl)
        val url: String = uri.authority + uri.path
        val queryParams: Set<String> = uri.queryParameterNames
        bundle.putString(PARAM_PUSH_CLICK_URL, url)
        for (paramName in queryParams) {
            // parameters are expected containing "utm" in the name
            if (paramName.startsWith("utm_")) {
                bundle.putString(paramName, uri.getQueryParameter(paramName))
            }
        }
        return bundle
    }
}