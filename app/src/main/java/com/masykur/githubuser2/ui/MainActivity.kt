package com.masykur.githubuser2.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.masykur.githubuser2.R
import com.masykur.githubuser2.adapter.UserAdapter
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.models.Item
import com.masykur.githubuser2.repository.UsersRepository
import com.masykur.githubuser2.utils.Resource
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    lateinit var userAdapter: UserAdapter
    val TAG = "User Data"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModelProviderFactory = GithubViewModelProviderFactory(
            application,
            UsersRepository(UserDetail(this)),
            this
        )
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(GithubViewModel::class.java)
        userAdapter = UserAdapter()
        ProgressBar.visibility = View.VISIBLE

        setupRV()
        viewModel.gitUsers.observe(this, Observer {
            userAdapter.differ.submitList(it.toList())
//            gitUsers.postValue(it)
            ProgressBar.visibility = View.INVISIBLE


        })
        userAdapter.setOnItemCLickListener {
            val moveWithObjectIntent = Intent(this@MainActivity, DetailActivity::class.java)
            moveWithObjectIntent.putExtra(DetailActivity.USER_DETAIL, it)
            startActivity(moveWithObjectIntent)
        }

    }

    private fun setupRV() {
        rvUsers.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {

                return true
            }


            var job: Job? = null
            override fun onQueryTextChange(newText: String): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    viewModel.getUserSearch(newText)

                }
                if (newText.isEmpty()) {
                    viewModel.getAllUsers()
                    setupRV()
                }

                viewModel.searchUser.observe(this@MainActivity, Observer {
                    when (it) {
                        is Resource.Succces -> {
                            ProgressBar.visibility = View.INVISIBLE
                            it.data?.let { searchResponse ->
                                userAdapter.differ.submitList(searchResponse.items.toList())
                                //plus dua ditambah supaya halaman sesuai
                                setupRV()

                            }
                        }
                        is Resource.Error -> {
                            ProgressBar.visibility = View.INVISIBLE
                            it.data.let {
                            }
                        }

                    }

                })
                return true
            }

        })
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu1) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        else if (item.itemId == R.id.menu2) {
            val mIntent = Intent(this@MainActivity, AlarmActivity::class.java)
            startActivity(mIntent)
        }
        else if (item.itemId == R.id.menu3) {
            val mIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }
}