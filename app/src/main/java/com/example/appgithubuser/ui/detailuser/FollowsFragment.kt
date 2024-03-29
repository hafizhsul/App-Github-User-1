package com.example.appgithubuser.ui.detailuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubuser.data.response.ItemsItem
import com.example.appgithubuser.databinding.FragmentFollowsBinding
import com.example.appgithubuser.ui.adapter.UserAdapter
import com.example.appgithubuser.ui.viewmodel.DetailUserViewModel
import com.example.appgithubuser.ui.viewmodel.FollowsViewModel
class FollowsFragment : Fragment() {

    private lateinit var binding: FragmentFollowsBinding
    private lateinit var adapter: UserAdapter
    private lateinit var viewModel: FollowsViewModel
    private lateinit var username: String

    companion object {
        private const val ARG_POSITION = "arg_position"
        private const val ARG_USERNAME = "arg_username"

        fun newInstance(position: Int, username: String): FollowsFragment {
            val fragment = FollowsFragment()
            val args = Bundle().apply {
                putInt(ARG_POSITION, position)
                putString(ARG_USERNAME, username)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = arguments?.getString(ARG_USERNAME) ?: ""
        adapter = UserAdapter { user ->

        }

        with(binding) {
            rvFollows.layoutManager = LinearLayoutManager(requireContext())
            rvFollows.adapter = adapter
        }

        viewModel = ViewModelProvider(requireActivity()).get(FollowsViewModel::class.java)
        showLoading(true)

        if (arguments?.getInt(ARG_POSITION) == 0) {
            showLoading(false)
            viewModel.getFollowers().observe(viewLifecycleOwner, { followers ->
                adapter.submitList(followers)
            })
            viewModel.fetchFollowers(username)
        } else {
            showLoading(false)
            viewModel.getFollowing().observe(viewLifecycleOwner, { following ->
                adapter.submitList(following)
            })
            viewModel.fetchFollowing(username)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}