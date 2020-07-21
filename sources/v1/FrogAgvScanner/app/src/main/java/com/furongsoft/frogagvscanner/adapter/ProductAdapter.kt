package com.furongsoft.frogagvscanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.furongsoft.frogagvscanner.databinding.ItemProductBinding
import com.furongsoft.frogagvscanner.entity.Product

/**
 * 产品适配器
 *
 * @author alex
 */
class ProductAdapter(private var list: List<Product>?) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val binding: ItemProductBinding? = DataBindingUtil.getBinding(holder.itemView)
        if (binding != null) {
            binding.model = list!![position]
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class ProductViewHolder(itemView: View) :
        ViewHolder(itemView)
}
