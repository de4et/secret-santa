package com.example.secret_santa.viewholder

import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
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
                val event = ServiceLocator.eventStorage.getById(item.id) ?: return@setOnClickListener
                if (event.participants.size <= 2) {
                    Toast.makeText(root.context, "Слишком мало участников!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                ServiceLocator.eventService.distributeInPairs(item.id)
                val lockedEvent = event.copy(isLocked = true)
                ServiceLocator.eventStorage.update(lockedEvent)
                Toast.makeText(root.context, "Пары успешно распределены", Toast.LENGTH_SHORT).show()
                val bundle = bundleOf(Constants.Keys.LIST_ITEM_DATA_KEY to item.id)
                val navController = root.findNavController()
                navController.navigate(R.id.action_mainFragment_to_listPageUserFragment, bundle)
            }
        }
    }
}
