package com.example.secret_santa.fragments.userlistpage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.secret_santa.adapter.ListPageAdapter
import com.example.secret_santa.databinding.FragmentListPageUserBinding
import com.example.secret_santa.decorator.BaseDecorator
import com.example.secret_santa.model.user.User
import com.example.secret_santa.storage.ServiceLocator
import com.example.secret_santa.utils.Constants

class ListPageUserFragment : Fragment() {
    private var viewBinding: FragmentListPageUserBinding? = null
    private var rvAdapter: ListPageAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentListPageUserBinding.inflate(inflater, container, false)
        return viewBinding?.root!!
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding?.listPageRv?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
            addItemDecoration(BaseDecorator(marginSize = 16))
        }

        val eventId = arguments?.getString(Constants.Keys.LIST_ITEM_DATA_KEY)
        val event = ServiceLocator.eventStorage.getById(eventId!!)
        val users = getParticipants(event?.participants)

        val usersMap = users.associateBy { it.id }

        rvAdapter = ListPageAdapter(
            dataList = users.toMutableList(),
            requestManager = Glide.with(this),
            onItemClickAdapter = { position ->
                val currentUser = users[position]
                val recipient = currentUser.recipientId?.let { recipientId ->
                    usersMap.values.find { it.id == recipientId }
                }
                val action = ListPageUserFragmentDirections.actionToDetails(recipient ?: currentUser)
                findNavController().navigate(action)
            }
        )

        viewBinding?.listPageRv?.adapter = rvAdapter
        viewBinding?.listPageRv?.addItemDecoration(BaseDecorator(marginSize = 16))
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