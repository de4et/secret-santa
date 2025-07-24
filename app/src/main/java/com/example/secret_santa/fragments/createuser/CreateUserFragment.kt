package com.example.secret_santa.fragments.createuser

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.secret_santa.R
import com.example.secret_santa.databinding.FragmentCreateUserBinding
import com.example.secret_santa.storage.ServiceLocator
import com.example.secret_santa.utils.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class CreateUserFragment : Fragment(R.layout.fragment_create_user) {

    private var viewBinding: FragmentCreateUserBinding? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                imageUri = uri
                viewBinding?.userIv?.setImageURI(uri)
                Toast.makeText(requireContext(), getString(R.string.image_saved), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), getString(R.string.image_not_choiced), Toast.LENGTH_SHORT).show()
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
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun onSaveUserButtonClick(view: View) {
        val eventId = arguments?.getString(Constants.Keys.EVENT_ID_KEY) ?: return
        val name = viewBinding?.userNameIe?.text.toString().trim()
        val wish = viewBinding?.userWishIe?.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.empty_username), Toast.LENGTH_SHORT).show()
            return
        }

        var path = "android.resource://com.example.secret_santa/drawable/baseline_person_200"
        if (imageUri?.toString() != path) {
            imageUri?.let {
                saveProfileImageToInternalStorage(
                    requireContext(),
                    it,
                    UUID.randomUUID().toString()
                )?.let { image ->
                    path = image
                }
            }
        }

        val id = ServiceLocator.userStorage.add(name, wish, path)
        val event = ServiceLocator.eventStorage.getById(eventId)
        val updatedEvent = event?.copy(participants = event.participants + id)
        if (updatedEvent != null) {
            ServiceLocator.eventStorage.update(updatedEvent)
            Toast.makeText(requireContext(), getString(R.string.user_saved), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.user_not_saved), Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }

    fun saveProfileImageToInternalStorage(
        context: Context,
        imageUri: Uri,
        userId: String
    ): String? {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            inputStream?.use { input ->
                val fileName = "profile_$userId"
                val file =
                    File(context.filesDir, fileName)

                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
                return file.absolutePath
            }
        } catch (e: IOException) {
            Toast.makeText(context, getString(R.string.error_image_save), Toast.LENGTH_SHORT).show()
        }
        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}