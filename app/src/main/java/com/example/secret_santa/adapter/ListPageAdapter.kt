package com.example.secret_santa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.secret_santa.databinding.ItemListUserPageBinding
import com.example.secret_santa.model.user.User
import com.example.secret_santa.viewholder.ListPageUserViewHolder

class ListPageAdapter(
    private val dataList: MutableList<User>,
    private val requestManager: RequestManager,
    private val onItemClickAdapter: (Int) -> Unit,
) : RecyclerView.Adapter<ListPageUserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListPageUserViewHolder {
        val viewBinding = ItemListUserPageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListPageUserViewHolder(
            viewBinding = viewBinding,
            requestManager = requestManager,
            onItemClickViewHolder = onItemClickAdapter,
        )
    }

    override fun onBindViewHolder(holder: ListPageUserViewHolder, position: Int) {
        holder.bindData(user = dataList[position], itemPosition = position, itemsCount = itemCount)
    }

    override fun getItemCount(): Int = dataList.size
}