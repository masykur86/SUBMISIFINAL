package com.masykur.githubuser2.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.google.android.material.snackbar.Snackbar
import com.masykur.githubuser2.ContentProvider.Companion.URI_USER
import com.masykur.githubuser2.R
import com.masykur.githubuser2.adapter.SectionsPagerAdapter
import com.masykur.githubuser2.adapter.UserAdapter
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.models.Item
import com.masykur.githubuser2.models.Item.Companion.COLUMN_AVATAR_URL
import com.masykur.githubuser2.models.Item.Companion.COLUMN_COMPANY
import com.masykur.githubuser2.models.Item.Companion.COLUMN_FOLLOWERS
import com.masykur.githubuser2.models.Item.Companion.COLUMN_FOLLOWING
import com.masykur.githubuser2.models.Item.Companion.COLUMN_ID
import com.masykur.githubuser2.models.Item.Companion.COLUMN_LOCATION
import com.masykur.githubuser2.models.Item.Companion.COLUMN_LOGIN
import com.masykur.githubuser2.models.Item.Companion.COLUMN_NAME
import com.masykur.githubuser2.models.Item.Companion.COLUMN_PUBLIC_REPOS
import com.masykur.githubuser2.repository.UsersRepository
import com.masykur.githubuser2.ui.fragment.FollowersFragment
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.time.nanoseconds

class DetailActivity : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    companion object {
        const val USER_DETAIL = "user_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val person = intent.getSerializableExtra(USER_DETAIL) as Item
        val viewModelProviderFactory = GithubViewModelProviderFactory(application,UsersRepository(UserDetail(this)),this)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(GithubViewModel::class.java)

        contentLoad()

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.hide()
        ivBack.setOnClickListener {
            val moveIntent = Intent(this, MainActivity::class.java)
            startActivity(moveIntent)

        }

        viewModel.getDetailUsers(person.login)
        viewModel.gitUsersDetail.observe(this, Observer {
            tvNama.text = it.login
            tvFollowersAngka.text = it.followers.toString()
            tvFollowingAngka.text = it.following.toString()
            tvRepoAngka.text = it.public_repos.toString()
            tvCompanyCountry.text = "${it.company},${it.location}"
            circleImageView.load(it.avatar_url)
            fabFavorite.setOnClickListener {view ->
                val values = ContentValues()
                if (it.company == null) { it.company =""}
                values.put(COLUMN_ID,it.id)
                values.put(COLUMN_AVATAR_URL,it.avatar_url)
                values.put(COLUMN_FOLLOWERS,it.followers)
                values.put(COLUMN_FOLLOWING,it.following)
                values.put(COLUMN_LOGIN,it.login)
                values.put(COLUMN_NAME,it.name)
                values.put(COLUMN_COMPANY,it.company)
                values.put(COLUMN_LOCATION,it.location)
                values.put(COLUMN_PUBLIC_REPOS,it.public_repos)
                contentResolver.insert(URI_USER,values)
                Snackbar.make(view,"Data Disimpan", Snackbar.LENGTH_SHORT).show()
               }
            loadFinish()
        })


    }

    private fun loadFinish() {
        ProgressBarDetail.visibility = View.INVISIBLE
        circleImageView.visibility = View.VISIBLE
        tvRepoAngka.visibility = View.VISIBLE
        tvRepo.visibility = View.VISIBLE
        tvCompanyCountry.visibility = View.VISIBLE
        tvNama.visibility = View.VISIBLE
        tvFollowing.visibility = View.VISIBLE
        tvFollowingAngka.visibility = View.VISIBLE
        tvFollowers.visibility = View.VISIBLE
        tvFollowersAngka.visibility = View.VISIBLE
    }

    private fun contentLoad() {
        ProgressBarDetail.visibility = View.VISIBLE
        circleImageView.visibility = View.INVISIBLE
        tvRepoAngka.visibility = View.INVISIBLE
        tvRepo.visibility = View.INVISIBLE
        tvCompanyCountry.visibility = View.INVISIBLE
        tvNama.visibility = View.INVISIBLE
        tvFollowing.visibility = View.INVISIBLE
        tvFollowingAngka.visibility = View.INVISIBLE
        tvFollowers.visibility = View.INVISIBLE
        tvFollowersAngka.visibility = View.INVISIBLE
    }

}