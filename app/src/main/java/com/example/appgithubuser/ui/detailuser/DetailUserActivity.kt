package com.example.appgithubuser.ui.detailuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.appgithubuser.data.response.DetailUserResponse
import com.example.appgithubuser.databinding.ActivityDetailUserBinding
import com.example.appgithubuser.ui.viewmodel.DetailUserViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel>()
    private lateinit var adapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val username = intent.getStringExtra("extra_username") ?: ""

        val mBundle = Bundle()
        mBundle.putString("username", username)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)

        adapter = SectionsPagerAdapter(this)
        adapter.username = username

        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabsLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Followers"
                1 -> "Following"
                else -> "Unknown"
            }
        }.attach()

        viewModel.getUserDetail(username.toString())
        viewModel.detailuser.observe(this) { detailUser ->
            if (detailUser != null) {
                setUserData(detailUser)
            }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setUserData(detailUser: DetailUserResponse) {
        Glide.with(this@DetailUserActivity)
            .load(detailUser.avatarUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .centerCrop()
            .circleCrop()
            .into(binding.imgDetailUser)
        if (detailUser.name != null) {
            binding.tvName.text = detailUser.name
        } else {
            binding.tvName.text = detailUser.login
        }
        binding.tvUsername.text = detailUser.login
        binding.tvFollowers.text = "${detailUser.followers} Followers"
        binding.tvFollowing.text = "${detailUser.following} Following"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}