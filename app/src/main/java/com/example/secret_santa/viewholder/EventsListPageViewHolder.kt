package com.example.secret_santa.viewholder

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.secret_santa.R
import com.example.secret_santa.databinding.EventsListItemBinding
import com.example.secret_santa.model.event.Event
import com.example.secret_santa.storage.ServiceLocator
import com.example.secret_santa.utils.Constants
import androidx.navigation.findNavController

class EventsListPageViewHolder(
    private val viewBinding: EventsListItemBinding,
    private val requestManager: RequestManager,
    private val onItemClickViewHolder: (Int) -> Unit,
    private val onPlayButtonClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.root.setOnClickListener {
            onItemClickViewHolder.invoke(adapterPosition)
        }

        viewBinding.playButton.setOnClickListener {
            onPlayButtonClick(adapterPosition)
        }
    }

    fun bindData(item: Event, itemPosition: Int, itemsCount: Int) {
        with(viewBinding) {
            eventName.text = item.name
            val positionText = if (itemPosition < 9) {
                "0${itemPosition + 1}"
            } else {
                "${itemPosition + 1}"
            }
            eventDate.text = item.dateStart
            eventNumberTv.text = positionText
        }
    }
}
