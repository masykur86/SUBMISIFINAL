package com.masykur.githubuser2.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.masykur.githubuser2.R
import com.masykur.githubuser2.adapter.UserAdapter
import com.masykur.githubuser2.ui.DetailActivity
import com.masykur.githubuser2.ui.FavoriteDetail
import com.masykur.githubuser2.ui.GithubViewModel
import kotlinx.android.synthetic.main.fragment_followers.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FollowersFragmentFav : Fragment(R.layout.fragment_followers) {
    var userName: String? = null
    lateinit var viewModel: GithubViewModel
    var job: Job? = null
    lateinit var userAdapter: UserAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FavoriteDetail).viewModel
        userAdapter = UserAdapter()
        viewModel.gitUsersDetail.observe(viewLifecycleOwner, Observer {
            userName = it.login
        })
        progressBarFollower.visibility = View.VISIBLE
        job?.cancel()
        job = MainScope().launch {
            delay(3000)
            viewModel.getUsersFollowers(userName!!)
            viewModel.gitUsersFollowers.observe(viewLifecycleOwner, Observer {
                userAdapter.differ.submitList(it.toList())
            })
            progressBarFollower.visibility = View.INVISIBLE
            setupRV()
        }


    }
    private fun setupRV() {
          userAdapter = UserAdapter()
        rvFollowList.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}