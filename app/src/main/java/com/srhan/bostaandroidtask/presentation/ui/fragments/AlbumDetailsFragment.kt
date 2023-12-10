package com.srhan.bostaandroidtask.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.srhan.bostaandroidtask.R
import com.srhan.bostaandroidtask.databinding.FragmentAlbumDetailsBinding
import com.srhan.bostaandroidtask.domain.model.Photo
import com.srhan.bostaandroidtask.presentation.adapters.PhotoAdapter
import com.srhan.bostaandroidtask.presentation.viewmodel.AlbumDetailsViewModel
import com.srhan.bostaandroidtask.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumDetailsFragment : Fragment() {

    private val viewModel: AlbumDetailsViewModel by viewModels()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var binding: FragmentAlbumDetailsBinding
    private val args: AlbumDetailsFragmentArgs by navArgs()
    lateinit var photoList: List<Photo>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        val albumId = args.albumId
        binding.tvAlbumDetailsName.text = args.albumName

        viewModel.getPhotos(albumId)


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.filterPhotos(photoList, newText)
                }

                return true
            }
        })

        photoAdapter.onImageClick = {
            val bundle = Bundle().apply {
                putString("url", it)
            }
            findNavController().navigate(
                R.id.action_albumDetailsFragment_to_imageFragment,
                bundle
            )
        }

        lifecycleScope.launch {
            viewModel.filteredPhotos.collect {
                photoAdapter.differ.submitList(it)
            }
        }
        lifecycleScope.launch {
            viewModel.photos.collect { result ->
                showProgressBar()
                when (result) {
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(
                            requireContext(),
                            result.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        result.data?.let { photoList ->
                            this@AlbumDetailsFragment.photoList = photoList
                            photoAdapter.differ.submitList(photoList)
                        }

                    }

                    else -> Unit
                }

            }
        }


    }


    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    private fun setUpRecyclerView() {
        photoAdapter = PhotoAdapter()
        binding.rvPhotos.apply {
            adapter = photoAdapter
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        }
    }

}