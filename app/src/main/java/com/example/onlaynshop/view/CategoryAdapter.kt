package com.example.onlaynshop.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.onlaynshop.R
import com.example.onlaynshop.model.CategoryModel
import kotlinx.android.synthetic.main.category_item.view.*
interface CategoryAdapterCallback{
    fun onClickItem(item:CategoryModel)
}
class CategoryAdapter(val items:List<CategoryModel>,val callback: CategoryAdapterCallback) :RecyclerView.Adapter<CategoryAdapter.ItemHolder>(){

    class ItemHolder(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = items[position]
        holder.itemView.setOnClickListener {
            items.forEach {
                it.checked = false
            }
            item.checked = true

            callback.onClickItem(item)
            notifyDataSetChanged()
        }
        holder.itemView.tvTitle.text = item.title
        if (item.checked){
            holder.itemView.cardView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.colorPrimary))
            holder.itemView.tvTitle.setTextColor(Color.WHITE)
        }else{
            holder.itemView.cardView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.grey))
            holder.itemView.tvTitle.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount(): Int {
        return items.count()
    }
}