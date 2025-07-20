package com.example.secret_santa

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.secret_santa.databinding.FragmentUserDetailsBinding
import com.example.secret_santa.model.User
import com.example.secret_santa.utils.Constants
import java.io.File

class DetailsUserFragment : Fragment(R.layout.fragment_user_details) {
    private var viewBinding: FragmentUserDetailsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentUserDetailsBinding.bind(view)
        val user = arguments?.getParcelable<User>(Constants.Args.USER) ?: return

        viewBinding!!.userNickname.text = user.username
        viewBinding!!.wishesText.text = user.wishes ?: getString(R.string.no_wishes_text)

        if (!user.pathToImage.isNullOrEmpty()) {
            Glide.with(this)
                .load(File(requireContext().filesDir, user.pathToImage))
                .into(viewBinding!!.userAvatar)
        }

        viewBinding!!.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}