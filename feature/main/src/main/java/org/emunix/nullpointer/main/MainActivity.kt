package org.emunix.nullpointer.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.core.api.domain.PreferencesProvider
import org.emunix.nullpointer.core.api.navigation.HistoryScreenLauncher
import org.emunix.nullpointer.core.api.navigation.SettingsScreenLauncher
import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher
import org.emunix.nullpointer.main.databinding.ActivityMainBinding
import org.emunix.nullpointer.main.di.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var uploadScreenLauncher: UploadScreenLauncher

    @Inject
    lateinit var historyScreenLauncher: HistoryScreenLauncher

    @Inject
    lateinit var settingsScreenLauncher: SettingsScreenLauncher

    @Inject
    lateinit var preferencesProvider: PreferencesProvider

    private lateinit var binding: ActivityMainBinding

    private val navView: BottomNavigationView by lazy { binding.navView }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        MainComponent.create((application as AppProviderHolder).appProvider).inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntent()

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_upload -> {
                    uploadScreenLauncher.launchUploadScreen(
                        R.id.nav_host_fragment_activity_main,
                        supportFragmentManager
                    )
                    true
                }
                R.id.navigation_history -> {
                    historyScreenLauncher.launchHistoryScreen(
                        R.id.nav_host_fragment_activity_main,
                        supportFragmentManager
                    )
                    true
                }
                R.id.navigation_settings -> {
                    settingsScreenLauncher.launchSettingsScreen(
                        R.id.nav_host_fragment_activity_main,
                        supportFragmentManager
                    )
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState != null) {
            navView.selectedItemId = savedInstanceState.getInt(SELECTED_SCREEN, R.id.navigation_upload)
        } else {
            navView.selectedItemId = R.id.navigation_upload
        }
    }

    private fun handleIntent() {
        if (intent?.action == Intent.ACTION_SEND) {
            (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { uri ->
                preferencesProvider.launchUri = uri.toString()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_SCREEN, navView.selectedItemId)
    }

    companion object {

        const val SELECTED_SCREEN = "SELECTED_SCREEN"
    }
}