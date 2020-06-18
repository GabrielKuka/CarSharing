package com.car.sharing.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.sharing.R
import com.car.sharing.databinding.FragmentAddEditPostBinding
import com.car.sharing.models.CarPhoto
import com.car.sharing.models.Post
import com.car.sharing.ui.adapters.CarPhotosAdapter
import com.car.sharing.utils.IAddEdit
import com.car.sharing.viewmodels.HomeViewModel
import com.meet.quicktoast.Quicktoast

class AddEditPost : Fragment(), IAddEdit, CarPhotosAdapter.CarPhotoInteraction {

    private lateinit var binder: FragmentAddEditPostBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var carPhotosAdapter: CarPhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        homeViewModel = requireActivity().run {
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        initObservers()

        binder.carName.requestFocus()

        binder.provideDone.setOnClickListener {
            val carName = binder.carName.editText?.text?.toString()?.trim()
            val carDescription = binder.carDescription.editText?.text?.toString()?.trim()
            val price = binder.price.editText?.text?.toString()?.trim()

            if (homeViewModel.areFieldsEmpty(listOf(carName, carDescription, price.toString()))) {
                Quicktoast(requireActivity()).swarn("There are empty fields.")
                return@setOnClickListener
            }

            if (homeViewModel.getCarPhotos().value.isNullOrEmpty()) {
                Quicktoast(requireActivity()).swarn("Add at least one photo of your car.")
                return@setOnClickListener
            }

            addPost(
                carName!!,
                carDescription!!,
                price!!.toDouble(),
                homeViewModel.getCarPhotos().value!!
            )

        }

        binder.uploadImageButton.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun getFileExtension(uri: Uri): String? {
        val cr = requireActivity().contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null && data.data != null) {
            val imageUri = data.data
            val imageName: String =
                System.currentTimeMillis().toString() + "." + getFileExtension(imageUri!!)

            val carPhoto = CarPhoto(imageName, imageUri.toString())
            homeViewModel.addCarPhoto(carPhoto)
        }

    }

    override fun onPhotoUploadError(msg: String) {
        Quicktoast(requireActivity()).swarn(msg)
    }

    private fun initRecyclerView() {
        carPhotosAdapter = CarPhotosAdapter(this)
        binder.carPhotosRv.adapter = carPhotosAdapter
        binder.carPhotosRv.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initObservers() {
        homeViewModel.isLoading().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binder.isLoading = it
        })

        homeViewModel.getCarPhotos().observe(viewLifecycleOwner, Observer {
            carPhotosAdapter.submitList(it)
        })

    }

    override fun removePhoto(carPhoto: CarPhoto) {
        homeViewModel.removeCarPhoto(carPhoto)
        carPhotosAdapter.notifyDataSetChanged()
    }

    private fun addPost(
        carName: String,
        carDescription: String,
        price: Double,
        photos: List<CarPhoto>
    ) {

        val id = homeViewModel.getPostRef().push().key!!

        // Add the post
        val post = Post(
            id,
            carName,
            carDescription,
            price,
            homeViewModel.getUser()?.displayName,
            homeViewModel.getUser()?.email!!
        )



        homeViewModel.addPost(post, photos, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binder =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_edit_post, container, false)

        return binder.root
    }

    override fun onSuccess(msg: String) {
        homeViewModel.setLoading()
        Quicktoast(requireActivity()).linfo(msg)
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onError(msg: String) {
        homeViewModel.setLoading()
        Quicktoast(requireActivity()).swarn(msg)
    }

    override fun onCarPhotoSelected(carPhoto: CarPhoto) {
        Quicktoast(requireActivity()).sinfo(carPhoto.name)
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

}
