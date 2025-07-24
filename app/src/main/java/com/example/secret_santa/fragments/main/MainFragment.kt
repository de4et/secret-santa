package com.example.secret_santa.fragments.main;

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secret_santa.R
import com.example.secret_santa.adapter.EventsPageAdapter
import com.example.secret_santa.databinding.FragmentMainBinding
import com.example.secret_santa.model.event.Event
import com.example.secret_santa.storage.ServiceLocator
import com.example.secret_santa.utils.Constants

class MainFragment : Fragment(R.layout.fragment_main) {

        private var viewBinding: FragmentMainBinding? = null

        private var rvAdapter: EventsPageAdapter? = null

        private var dataList: MutableList<Event>? = null

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                viewBinding = FragmentMainBinding.bind(view)
                initViews()
        }

        private fun initViews() {
                if (rvAdapter == null) {
                        if (dataList == null) {
                                dataList = ServiceLocator.eventStorage.getAll().toMutableList()
                        }
                        rvAdapter = EventsPageAdapter(
                                dataList = dataList ?: mutableListOf(),
                                requestManager = Glide.with(this),
                                onItemClickAdapter = ::onItemClick,
                                onPlayButtonClick = ::onPlayButtonClick,
                                onDeleteButtonClick = ::onDeleteButtonClick
                        )
                }

                val layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                viewBinding?.listPageRv?.apply {
                        this.layoutManager = layoutManager
                        adapter = rvAdapter
                }
                viewBinding?.addEventButton?.setOnClickListener {
                        findNavController()
                                .navigate(R.id.action_mainFragment_to_createEventFragment)
                }
                updateEmptyListTv()
        }

        private fun onItemClick(position: Int) {
                val item = dataList?.get(position) ?: return
                val bundle = bundleOf(Constants.Keys.LIST_ITEM_DATA_KEY to item.id)

                if (!item.isLocked) {
                        findNavController()
                                .navigate(R.id.action_mainFragment_to_eventFragment, bundle)
                } else {
                        findNavController()
                                .navigate(R.id.action_mainFragment_to_listPageUserFragment, bundle)
                }
        }

        private fun onPlayButtonClick(position: Int) {
                val item = dataList?.get(position) ?: return
                val event = ServiceLocator.eventStorage.getById(item.id) ?: return
                if (event.isLocked) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.event_is_locked_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                }
                if (event.participants.size <= 2) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.not_enough_users_toast),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                }
                ServiceLocator.eventService.distributeInPairs(item.id)
                val lockedEvent = event.copy(isLocked = true)
                ServiceLocator.eventStorage.update(lockedEvent)
                dataList?.let { safeDataList ->
                        safeDataList[position].isLocked = true
                        rvAdapter?.notifyItemChanged(position)
                }
                Toast.makeText(
                    requireContext(),
                    getString(R.string.users_paired_toast),
                    Toast.LENGTH_SHORT
                ).show()
                val bundle = bundleOf(Constants.Keys.LIST_ITEM_DATA_KEY to item.id)
                val navController = findNavController()
                navController.navigate(R.id.action_mainFragment_to_listPageUserFragment, bundle)
        }

        private fun onDeleteButtonClick(position: Int) {
                val item = dataList?.get(position) ?: return
                ServiceLocator.eventStorage.delete(item.id)
                dataList?.removeAt(position)
                rvAdapter?.apply {
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, itemCount)
                }
                updateEmptyListTv()
        }

        private fun updateEmptyListTv() {
            if (dataList?.isEmpty() == true) {
                viewBinding?.emptyTv?.visibility = View.VISIBLE
            } else {
                viewBinding?.emptyTv?.visibility = View.GONE
            }
        }

    override fun onResume() {
        super.onResume()
        val updatedList = ServiceLocator.eventStorage.getAll()

        dataList?.let { safeDataList ->
            if (safeDataList.size < updatedList.size) {
                val event = ServiceLocator.eventStorage.getById(updatedList.last().id)
                if (event != null) {
                    safeDataList.add(event)
                }
                rvAdapter?.notifyItemInserted(safeDataList.size - 1)
            }
        }
        updateEmptyListTv()
    }

        override fun onDestroyView() {
                super.onDestroyView()
                viewBinding = null
        }
}
