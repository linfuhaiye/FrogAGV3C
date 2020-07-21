package com.furongsoft.frogagvscanner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.furongsoft.frogagvscanner.databinding.ItemMaterialBinding
import com.furongsoft.frogagvscanner.entity.Material

/**
 * 原料适配器
 *
 * @author alex
 */
class MaterialAdapter(private var list: List<Material>?) :
    RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding =
            ItemMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val binding: ItemMaterialBinding? = DataBindingUtil.getBinding(holder.itemView)
        if (binding != null) {
            binding.model = list!![position]
            binding.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    class MaterialViewHolder(itemView: View) :
        ViewHolder(itemView)
}
