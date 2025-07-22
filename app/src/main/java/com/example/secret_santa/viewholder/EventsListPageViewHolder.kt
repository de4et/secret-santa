package com.example.secret_santa.viewholder

import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.secret_santa.R
import com.example.secret_santa.databinding.EventsListItemBinding
import com.example.secret_santa.model.event.Event
import com.example.secret_santa.utils.Constants

class EventsListPageViewHolder(
    private val viewBinding: EventsListItemBinding,
    private val requestManager: RequestManager,
    private val onItemClickViewHolder: (Int) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

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
            root.setOnClickListener {
                onItemClickViewHolder.invoke(adapterPosition)
            }
            playButton.setOnClickListener {
                val bundle = bundleOf(Constants.Keys.LIST_ITEM_DATA_KEY to item)
//                findNavController()
//                        .navigate(R.id.action_mainFragment_to_kakoytoFragment, bundle)
            }
        }
    }
}
