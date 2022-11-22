package com.example.onlineshop.screen

import android.content.ContentValues.TAG
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.onlaynshop.R
import com.example.onlaynshop.databinding.ActivityMainBinding
import com.example.onlaynshop.db.AppDatabase
import com.example.onlaynshop.screen.MainViewModel
import com.example.onlaynshop.screen.cart.CartFragment
import com.example.onlaynshop.screen.changelanguage.ChangeLanguageFragment
import com.example.onlaynshop.screen.favorite.FavoriteFragment
import com.example.onlaynshop.screen.profile.ProfileFragment
import com.example.onlaynshop.utils.LocalManager
import com.example.onlineshop.screen.home.HomeFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val ONESIGNAL_APP_ID = "d40aeffc-8d07-4cb8-9dc7-bd6dc9d21e37";

    lateinit var binding: ActivityMainBinding
    val homeFragment = HomeFragment.newInstance()
    val favoriteFragment = FavoriteFragment.newInstance()
    val cartFragment = CartFragment.newInstance()
    val profileFragment = ProfileFragment.newInstance()
    var activeFragment:Fragment = homeFragment
    lateinit var viewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

            // OneSignal Initialization
            OneSignal.initWithContext(this);
            OneSignal.setAppId(ONESIGNAL_APP_ID);

            // promptForPushNotifications will show the native Android notification permission prompt.
            // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
            OneSignal.promptForPushNotifications();
        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d(TAG, msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }

        viewModel = MainViewModel()

        viewModel.productData.observe(this, Observer {
            viewModel.insertAllProducts2DB(it)
        })
        viewModel.categoriesData.observe(this, Observer {
            viewModel.insertAllCategories2DB(it)
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })


        supportFragmentManager.beginTransaction().add(R.id.flContainer,homeFragment,homeFragment.tag).hide(homeFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.flContainer,favoriteFragment,favoriteFragment.tag).hide(favoriteFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.flContainer,cartFragment,cartFragment.tag).hide(cartFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.flContainer,profileFragment,profileFragment.tag).hide(profileFragment).commit()


        supportFragmentManager.beginTransaction().show(activeFragment).commit()

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.actionHome){
                supportFragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                activeFragment = homeFragment
            }else if (it.itemId == R.id.actionFavorite){
                supportFragmentManager.beginTransaction().hide(activeFragment).show(favoriteFragment).commit()
                activeFragment = favoriteFragment
            }else if (it.itemId == R.id.actionCart){
                supportFragmentManager.beginTransaction().hide(activeFragment).show(cartFragment).commit()
                activeFragment = cartFragment
            }else if (it.itemId == R.id.actionProfile){
                supportFragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit()
                activeFragment = profileFragment
            }
            return@setOnNavigationItemSelectedListener true
        }
        btnMenu.setOnClickListener {
            val fragment = ChangeLanguageFragment.newInstance()
            fragment.show(supportFragmentManager,fragment.tag)
        }
    }

    fun loadData(){
        viewModel.getTopProducts()
        viewModel.getCategories()
    }
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocalManager.setLocale(newBase))
    }
}