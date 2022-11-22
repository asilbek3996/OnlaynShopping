package com.example.onlaynshop.api

import com.example.onlaynshop.model.BaseResponse
import com.example.onlaynshop.model.CategoryModel
import com.example.onlaynshop.model.OfferModel
import com.example.onlaynshop.model.ProductModel
import com.example.onlaynshop.model.request.GetProductsByIdsRequest
import com.example.onlaynshop.model.request.MakeOrderRequest
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {
    @GET("get_offers")
    fun getOffers(): Observable<BaseResponse<List<OfferModel>>>

    @GET("get_categories")
    fun getCategories(): Observable<BaseResponse<List<CategoryModel>>>

    @GET("get_top_products")
    fun getTopProducts(): Observable<BaseResponse<List<ProductModel>>>

    @GET("get_products/{category_id")
    fun getCategoryProducts(@Path("category_id") categoryId:Int): Observable<BaseResponse<List<ProductModel>>>


    @POST("get_products_by_ids")
    fun getProductsByIds(@Body request: GetProductsByIdsRequest): Observable<BaseResponse<List<ProductModel>>>


    @POST("make_order")
    fun makeOrder(@Body request: MakeOrderRequest): Observable<BaseResponse<Any>>
}