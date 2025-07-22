package com.example.secret_santa.viewholder

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.secret_santa.databinding.ItemListUserPageBinding
import com.example.secret_santa.model.User
import java.io.File

class ListPageUserViewHolder(
    private val viewBinding: ItemListUserPageBinding,
    private val requestManager: RequestManager,
    private val onItemClickViewHolder: (Int) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.root.setOnClickListener {
            onItemClickViewHolder(adapterPosition)
        }
    }

    fun bindData(user: User, itemPosition: Int, itemsCount: Int) {
        with(viewBinding) {
            listItemUsername.text = user.username
            itemDivider.isVisible = itemPosition != itemsCount - 1

            if (user.pathToImage != null) {
                val file = File(viewBinding.root.context.filesDir, user.pathToImage)
                requestManager
                    .load(file)
                    .into(listItemIv)
            }
        }
    }
}