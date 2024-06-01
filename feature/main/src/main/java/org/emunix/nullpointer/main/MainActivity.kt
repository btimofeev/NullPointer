package org.emunix.nullpointer.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.emunix.nullpointer.core.api.di.AppProviderHolder
import org.emunix.nullpointer.core.api.navigation.UploadScreenLauncher
import org.emunix.nullpointer.main.databinding.ActivityMainBinding
import org.emunix.nullpointer.main.di.MainComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var uploadScreenLauncher: UploadScreenLauncher

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainComponent.create((application as AppProviderHolder).appProvider).inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_upload -> {
                    uploadScreenLauncher.launchUploadScreen(
                        R.id.nav_host_fragment_activity_main,
                        supportFragmentManager
                    )
                    true
                }
                else -> false
            }
        }

        navView.selectedItemId = R.id.navigation_upload
    }
}