package com.example.onlaynshop.screen.cart

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlaynshop.R
import com.example.onlaynshop.model.ProductModel
import com.example.onlaynshop.screen.MainViewModel
import com.example.onlaynshop.utils.Constants
import com.example.onlaynshop.utils.PrefUtils
import com.example.onlaynshop.view.CartAdapter
import kotlinx.android.synthetic.main.fragment_cart.*
import uz.bdmgroup.onlineshop.screen.makeorder.MakeOrderActivity
import java.io.Serializable

class CartFragment : Fragment() {
    lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.progress.observe(this, Observer {
            swipe.isRefreshing = it
        })
        viewModel.error.observe(this, Observer {
            Toast.makeText(requireActivity(),it,Toast.LENGTH_LONG).show()
        })

        viewModel.productData.observe(this, Observer {
            recycler.adapter = CartAdapter(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.layoutManager = LinearLayoutManager(requireActivity())
        swipe.setOnRefreshListener {
            loadData()
        }

        btnMakeOrder.setOnClickListener {
            val intent = Intent(requireActivity(), MakeOrderActivity::class.java)
            intent.putExtra(Constants.EXTRA_DATA,(viewModel.productData.value ?: emptyList<ProductModel>()) as Serializable)
            startActivity(intent)
        }
        loadData()
    }



    fun loadData(){
        viewModel.getProductsByIds(PrefUtils.getCartList().map { it.product_id })
    }

    companion object {
        @JvmStatic
        fun newInstance() = CartFragment()
    }
}