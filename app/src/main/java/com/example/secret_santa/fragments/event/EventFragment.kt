package com.example.secret_santa.fragments.event

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.secret_santa.R
import com.example.secret_santa.adapter.ListPageAdapter
import com.example.secret_santa.databinding.FragmentEventBinding
import com.example.secret_santa.model.event.Event
import com.example.secret_santa.model.user.User
import com.example.secret_santa.storage.ServiceLocator
import com.example.secret_santa.util.Keys
import com.example.secret_santa.utils.getParcelable

class EventFragment : Fragment(R.layout.fragment_event) {

    var viewBinding: FragmentEventBinding? = null

    var rvAdapter: ListPageAdapter? = null

    var dataList: MutableList<User>? = null

    var event: Event? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentEventBinding.bind(view)
        initView()
    }

    private fun initView() {
        event = getParcelable<Event>(Keys.LIST_ITEM_DATA_KEY) ?: return

        if (rvAdapter == null) {
            if (dataList == null) {
                dataList = getParticipants(event?.participants).toMutableList()
            }

            rvAdapter = ListPageAdapter(
                dataList = dataList ?: mutableListOf(),
                onItemClickAdapter = { _ -> },
                requestManager = Glide.with(this)
            )
        }
        val layoutManger = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        viewBinding?.apply {
            eventUsersRv.layoutManager = layoutManger
            eventUsersRv.adapter = rvAdapter
            eventNameTv.text = event?.name
            addUserBtn.setOnClickListener(::onAddUserButtonClick)
        }

        // Return to main on back pressed
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_eventFragment_to_mainFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun onAddUserButtonClick(view: View) {
        findNavController().navigate(
            R.id.action_eventFragment_to_createUserFragment,
            bundleOf(Keys.EVENT_ID_KEY to event?.id)
        )
    }

    private fun getParticipants(ids: List<String>?): List<User> {
        ids?.let { safeIds ->
            val idSet = safeIds.toSet()
            return ServiceLocator.userStorage.getAll().filter { user ->
                idSet.contains(user.id)
            }
        }

        return listOf()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}