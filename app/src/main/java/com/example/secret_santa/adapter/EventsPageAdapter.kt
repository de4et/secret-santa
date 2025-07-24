package com.example.secret_santa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.secret_santa.databinding.EventsListItemBinding
import com.example.secret_santa.model.event.Event
import com.example.secret_santa.viewholder.EventsListPageViewHolder

class EventsPageAdapter(
    private val dataList: MutableList<Event>,
    private val requestManager: RequestManager,
    private val onItemClickAdapter: (Int) -> Unit,
    private val onPlayButtonClick: (Int) -> Unit,
    private val onDeleteButtonClick: (Int) -> Unit
) : RecyclerView.Adapter<EventsListPageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListPageViewHolder {
        val viewBinding = EventsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EventsListPageViewHolder(
            viewBinding = viewBinding,
            requestManager = requestManager,
            onItemClickViewHolder = onItemClickAdapter,
            onPlayButtonClick = onPlayButtonClick,
            onDeleteButtonClick = onDeleteButtonClick
        )
    }

    override fun onBindViewHolder(holder: EventsListPageViewHolder, position: Int) {
        holder.bindData(item = dataList[position], itemPosition = position, itemsCount = itemCount)
    }

    override fun getItemCount(): Int = dataList.size
}
