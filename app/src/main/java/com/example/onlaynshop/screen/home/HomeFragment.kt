package com.example.onlineshop.screen.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.onlaynshop.R
import com.example.onlaynshop.api.Api
import com.example.onlaynshop.model.BaseResponse
import com.example.onlaynshop.model.CategoryModel
import com.example.onlaynshop.model.OfferModel
import com.example.onlaynshop.screen.MainViewModel
import com.example.onlaynshop.view.CategoryAdapter
import com.example.onlaynshop.view.CategoryAdapterCallback
import com.example.onlaynshop.view.ProductAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    var offers :List<OfferModel> = emptyList()
    lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe.setOnRefreshListener {
            loadData()
        }

        recyclerCategories.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        recyclerProducts.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)


        viewModel.error.observe(requireActivity(), Observer {
            Toast.makeText(requireActivity(),it,Toast.LENGTH_LONG).show()
        })

        viewModel.progress.observe(requireActivity(), Observer {
            swipe.isRefreshing = it
        })
        viewModel.offersData.observe(requireActivity(), Observer {
            carouselView.setImageListener { position, imageView ->
                Glide.with(imageView).load("http://osonsavdo.herokuapp.com/images/${it[position].image}").into(imageView)
            }
            carouselView.pageCount = it.count()
        })



        viewModel.categoriesData.observe(requireActivity(), Observer {
            recyclerCategories.adapter = CategoryAdapter(it,object : CategoryAdapterCallback{
                override fun onClickItem(item: CategoryModel) {
                    viewModel.getProductsByCategory(item.id)
                }
            })
        })
        viewModel.productData.observe(requireActivity(), Observer {
            recyclerProducts.adapter = ProductAdapter(it)

        })

        loadData()

    }
    fun  loadData(){
        viewModel.getOffers()
        viewModel.getAllDBCategories()
        viewModel.getAllDBProducts()
//        viewModel.getTopProducts()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}