package com.example.secret_santa.fragments.userdetails

import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.secret_santa.databinding.FragmentUserDetailsBinding
import com.example.secret_santa.model.user.User
import com.example.secret_santa.utils.Constants
import androidx.core.net.toUri
import com.example.secret_santa.R

class DetailsUserFragment : Fragment(R.layout.fragment_user_details) {
    private var viewBinding: FragmentUserDetailsBinding? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentUserDetailsBinding.bind(view)
        val user = arguments?.getParcelable(Constants.Args.USER, User::class.java) ?: return

        viewBinding?.userNickname?.text = user.name
        val text = if (user.wishes.isNullOrEmpty())
            getString(R.string.no_wishes_text)
        else
            user.wishes
        viewBinding?.wishesText?.text = text

        if (!user.pathToImage.isNullOrEmpty()) {
            val uri = user.pathToImage.toUri()
            viewBinding?.userAvatar?.setImageURI(uri)
//            Glide.with(this)
//                .load(uri)
//                .into(viewBinding?.userAvatar!!)
        }
        viewBinding?.wishesText?.movementMethod = ScrollingMovementMethod.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}