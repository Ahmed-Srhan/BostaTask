package com.srhan.bostaandroidtask.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.srhan.bostaandroidtask.R
import com.srhan.bostaandroidtask.databinding.FragmentProfileBinding
import com.srhan.bostaandroidtask.presentation.adapters.AlbumAdapter
import com.srhan.bostaandroidtask.presentation.viewmodel.ProfileViewModel
import com.srhan.bostaandroidtask.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var albumsAdapter: AlbumAdapter
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        lifecycleScope.launch {
            viewModel.user.collect { result ->
                showProgressBar()
                when (result) {
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        result.data?.let { user ->
                            binding.tvName.text = user.name
                            binding.tvAddress.text = user.address.toString()
                            viewModel.getAlbums(user.id)

                        }
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.albums.collect { result ->
                showProgressBar()
                when (result) {
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Loading -> {
                        showProgressBar()
                    }

                    is Resource.Success -> {
                        hideProgressBar()
                        result.data?.let { albumList ->
                            albumsAdapter.differ.submitList(albumList)

                        }
                    }

                    else -> Unit
                }
            }

        }


        albumsAdapter.onAlbumClick = {
            val bundle = Bundle().apply {
                putInt("albumId", it.id)
                putString("albumName", it.title)
            }
            findNavController().navigate(
                R.id.action_profileFragment_to_albumDetailsFragment,
                bundle
            )
        }
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


    private fun setUpRecyclerView() {
        albumsAdapter = AlbumAdapter()
        binding.rvAlbums.apply {
            adapter = albumsAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }


}