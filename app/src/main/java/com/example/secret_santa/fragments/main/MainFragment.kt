package com.example.secret_santa.fragments.main;

import android.os.Bundle
import android.view.View
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
import com.example.secret_santa.util.Keys


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
                                dataList = ServiceLocator.eventStorage.getAll() as MutableList<Event>?
                        }
                        rvAdapter = EventsPageAdapter(
                                dataList = dataList ?: mutableListOf(),
                                requestManager = Glide.with(this),
                                onItemClickAdapter = ::onItemClick,
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
//                                .navigate(R.id.action_mainFragment_to_kakoytoFragment)

                }
        }

        private fun onItemClick(position: Int) {
                val item = dataList?.get(position) ?: return
                val bundle = bundleOf(Keys.LIST_ITEM_DATA_KEY to item)

//                findNavController()
//                        .navigate(R.id.action_mainFragment_to_kakoytoFragment, bundle)
        }
        override fun onDestroyView() {
                super.onDestroyView()
                viewBinding = null
        }
}
