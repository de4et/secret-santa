package com.example.secret_santa.viewholder

import android.util.Log
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.secret_santa.databinding.ItemListUserPageBinding
import com.example.secret_santa.model.user.User
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

    fun bindData(
        user: User,
        itemPosition: Int,
        itemsCount: Int,
        showDeleteButton: Boolean = false
    ) {
        with(viewBinding) {
            listItemUsername.text = user.name
            itemDivider.isVisible = itemPosition != itemsCount - 1
            deleteBtn.visibility = if (showDeleteButton) View.VISIBLE else View.GONE

            if (user.pathToImage != null) {
                listItemIv.setImageURI(user.pathToImage.toUri())
                Log.i("SET", "IMAGE SET ${user.pathToImage}")
//                val file = File(viewBinding.root.context.filesDir, user.pathToImage)
//                requestManager
//                    .load(file)
//                    .into(listItemIv)
            }
        }
    }
}