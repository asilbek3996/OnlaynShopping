package uz.bdmgroup.onlineshop.screen.makeorder

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.onlaynshop.MapsActivity
import com.example.onlaynshop.R
import com.example.onlaynshop.model.AdaressModel
import com.example.onlaynshop.model.CartModel
import com.example.onlaynshop.model.ProductModel
import com.example.onlaynshop.screen.MainViewModel
import com.example.onlaynshop.utils.Constants
import kotlinx.android.synthetic.main.activity_make_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MakeOrderActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    var address: AdaressModel? = null
    lateinit var items: List<ProductModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_order)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })




        items = intent.getSerializableExtra(Constants.EXTRA_DATA) as List<ProductModel>

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }

        tvTotalAmount.setText(items.sumByDouble { it.cartCount.toDouble() * (it.price.replace(" ", "").toDoubleOrNull() ?: 0.0) }.toString())

        eddAddress.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }

        btnMakeOrder.setOnClickListener {
            viewModel.makeOrder(items.map { CartModel(it.id, it.cartCount) }, address?.latitude ?: 0.0, address?.longitude ?: 0.0, eddComment.editText.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe
    fun onEvent(address: AdaressModel){
        this.address = address
        eddAddress.setText("${address.latitude}, ${address.longitude}")
    }
}