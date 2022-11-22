package com.example.onlaynshop.screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.onlaynshop.api.repository.ShopRepository
import com.example.onlaynshop.db.AppDatabase
import com.example.onlaynshop.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {
    val repository = ShopRepository()



    val error = MutableLiveData<String>()
    val progress = MutableLiveData<Boolean>()
    val offersData = MutableLiveData<List<OfferModel>>()
    val categoriesData = MutableLiveData<List<CategoryModel>>()
    val productData = MutableLiveData<List<ProductModel>>()
    val makeOrderData = MutableLiveData<Boolean>()

    fun getOffers(){
        repository.getOffers(error,progress,offersData)
    }
    fun getCategories(){
        repository.getCategories(error,categoriesData)
    }
    fun getTopProducts(){
        repository.getTopProducts(error,productData)
    }
    fun getProductsByCategory(id:Int){
        repository.getProductsByCategory(id,error,productData)
    }
    fun getProductsByIds(ids: List<Int>){
        repository.getProductsByIds(ids,error,progress,productData)
    }
    fun makeOrder(products:List<CartModel>, lat : Double, lon:Double, comment:String){
        repository.makeOrder(products, lat,lon, comment,error,progress,makeOrderData)
    }

    fun insertAllProducts2DB(items:List<ProductModel>){
        CoroutineScope(Dispatchers.IO).launch{
            AppDatabase.getDatabase().getProductDao().deleteAll()
            AppDatabase.getDatabase().getProductDao().insertAll(items)
        }
    }
    fun insertAllCategories2DB(items:List<CategoryModel>){
        CoroutineScope(Dispatchers.IO).launch{
            AppDatabase.getDatabase().getCategoryDao().deleteAll()
            AppDatabase.getDatabase().getCategoryDao().insetAll(items)
        }
    }

    fun getAllDBProducts(){
        CoroutineScope(Dispatchers.Main).launch {
            productData.value = withContext(Dispatchers.IO){AppDatabase.getDatabase().getProductDao().getAllProducts()}
        }
    }
    fun getAllDBCategories(){
        CoroutineScope(Dispatchers.Main).launch {
            categoriesData.value = withContext(Dispatchers.IO){AppDatabase.getDatabase().getCategoryDao().getAllCategories()}
        }
    }

}
