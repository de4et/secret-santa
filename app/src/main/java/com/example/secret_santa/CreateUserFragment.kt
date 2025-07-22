package com.example.secret_santa

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.secret_santa.databinding.FragmentCreateUserBinding
import com.example.secret_santa.storage.ServiceLocator

class CreateUserFragment : Fragment(R.layout.fragment_create_user) {

    private var viewBinding: FragmentCreateUserBinding? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Registers a photo picker activity launcher in single-select mode.
        pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                viewBinding?.userIv?.setImageURI(uri)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentCreateUserBinding.bind(view)
        initView()
    }

    private fun initView() {
        viewBinding?.apply {
            imageUri =
                "android.resource://com.example.secret_santa/drawable/baseline_person_200".toUri()
            userIv.setImageURI(imageUri)
            pickImageBtn.setOnClickListener(::onImagePickerClick)
            saveUserBtn.setOnClickListener(::onSaveUserButtonClick)
        }
    }

    private fun onImagePickerClick(view: View) {
        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
    }

    private fun onSaveUserButtonClick(view: View) {
        val name = viewBinding?.userNameIe?.text.toString().trim()
        val wish = viewBinding?.userWishIe?.text.toString().trim()
        val id = ServiceLocator.userStorage.add(name, wish, imageUri.toString())

        // TODO: add transition to next fragment
        Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}