package com.bangkit.intermediate.storyappfinal.presentation.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.asLiveData
import com.bangkit.intermediate.storyappfinal.R
import com.bangkit.intermediate.storyappfinal.core.feature.auth.data.preferences.UserPreference
import com.bangkit.intermediate.storyappfinal.core.util.Internet
import com.bangkit.intermediate.storyappfinal.databinding.ActivityMainBinding
import com.bangkit.intermediate.storyappfinal.presentation.ui.fragment.StoryFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var token: String? = null

    private lateinit var userPref: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPref = UserPreference(applicationContext)

        if (Internet.isAvailable(this@MainActivity)) {
            if (supportFragmentManager.findFragmentByTag("FRAGMENT_STORY") == null) {
                if (token == null) {
                    getToken()
                }
            }
        } else {
            Toast.makeText(this@MainActivity, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            binding.ibReload.visibility = View.VISIBLE
        }

        binding.apply {
            ibReload.setOnClickListener {
                if (Internet.isAvailable(this@MainActivity)) {
                    binding.ibReload.visibility = View.GONE
                    getToken()
                } else {
                    Toast.makeText(this@MainActivity, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                }
            }

            fabAddStory.setOnClickListener {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java)
                    .putExtra(AddStoryActivity.EXTRA_TOKEN, token))
                finish()
            }
        }

    }

    private fun getToken() {
        userPref.getUser().asLiveData().observe(this@MainActivity) { user ->
            user?.token.let { token ->
                if (token.isNullOrEmpty()) {
                    startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                    this@MainActivity.finish()
                } else {
                    this@MainActivity.token = token
                }
                StoryFragment.token = token
                supportFragmentManager.beginTransaction()
                    .add(R.id.story_fragment, StoryFragment(), "FRAGMENT_STORY")
                    .commitNow()
                Log.d("StoryFragment", " : Add Fragment to Container")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.story_map -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java)
                    .putExtra(MapsActivity.EXTRA_TOKEN, token))
                finish()
                true
            }
            R.id.settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(resources.getString(R.string.ask_logout))

                builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        userPref.setUser(null)
                    }
                    finish()
                }

                builder.setNegativeButton(resources.getString(R.string.no)) { _, _ ->
                }

                builder.show()
                true
            }
            else -> true
        }
    }
}