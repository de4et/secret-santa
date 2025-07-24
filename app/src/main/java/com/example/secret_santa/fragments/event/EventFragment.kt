package com.example.secret_santa.fragments.event

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
import com.example.secret_santa.adapter.ListPageAdapter
import com.example.secret_santa.databinding.FragmentEventBinding
import com.example.secret_santa.model.event.Event
import com.example.secret_santa.model.user.User
import com.example.secret_santa.storage.ServiceLocator
import com.example.secret_santa.utils.Constants
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
        event = ServiceLocator.eventStorage.getById(
            getParcelable<Event>(Constants.Keys.LIST_ITEM_DATA_KEY)?.id ?: return
        )

        if (rvAdapter == null) {
            if (dataList == null) {
                dataList = getParticipants(event?.participants).toMutableList()
            }

            rvAdapter = ListPageAdapter(
                dataList = dataList ?: mutableListOf(),
                onItemClickAdapter = { _ -> },
                requestManager = Glide.with(this),
                showDeleteButton = true,
                onDeleteButtonClick = ::onDeleteButtonClick
            )
        }
        val layoutManger = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        viewBinding?.apply {
            eventUsersRv.layoutManager = layoutManger
            eventUsersRv.adapter = rvAdapter
            eventNameTv.text = event?.name
            addUserBtn.setOnClickListener(::onAddUserButtonClick)
        }
    }

    override fun onResume() {
        super.onResume()
        event = ServiceLocator.eventStorage.getById(event?.id ?: return) ?: return

        // Update data list and rv if user was added
        dataList?.let { safeDataList ->
            event?.participants?.let { safeParticipants ->
                if (safeDataList.size < safeParticipants.size) {
                    val user = ServiceLocator.userStorage.getById(safeParticipants.last())
                    if (user != null) {
                        safeDataList.add(user)
                    }
                    rvAdapter?.notifyItemInserted(safeDataList.size - 1)
                }
            }
        }
    }

    private fun onAddUserButtonClick(view: View) {
        event?.apply {
            if (isLocked) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.event_is_locked_toast),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        findNavController().navigate(
            R.id.action_eventFragment_to_createUserFragment,
            bundleOf(Constants.Keys.EVENT_ID_KEY to event?.id)
        )
    }

    private fun onDeleteButtonClick(position: Int) {
        event?.apply {
            if (isLocked) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.event_is_locked_toast),
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        val user = dataList?.get(position) ?: return
        ServiceLocator.userStorage.delete(user.id)
        val newEvent = event?.copy(participants = event?.participants?.minus(user.id) ?: listOf())
        newEvent?.let {
            ServiceLocator.eventStorage.update(it)
        }
        dataList?.removeAt(position)
        rvAdapter?.notifyItemRemoved(position)
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