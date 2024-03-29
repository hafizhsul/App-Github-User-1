package com.example.appgithubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appgithubuser.data.response.ItemsItem
import com.example.appgithubuser.databinding.ActivityMainBinding
import com.example.appgithubuser.ui.adapter.UserAdapter
import com.example.appgithubuser.ui.detailuser.DetailUserActivity
import com.example.appgithubuser.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    viewModel.getUser(searchView.text.toString())
                    false
                }
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.userData.observe(this) {
            if (it != null) {
                setUserData(it)
            }
        }
    }

    private fun setUserData(users: List<ItemsItem>) {
        val adapter = UserAdapter { user ->
            onItemClick(user)
        }
        adapter.submitList(users)
        binding.rvUser.adapter = adapter
    }

    private fun onItemClick(user: ItemsItem) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra("extra_username", user.login)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}