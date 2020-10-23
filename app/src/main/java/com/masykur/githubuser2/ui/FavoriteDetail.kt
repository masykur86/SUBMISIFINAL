package com.masykur.githubuser2.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.masykur.githubuser2.R
import com.masykur.githubuser2.adapter.SectionsPagerAdapterFavorite
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.models.Item
import com.masykur.githubuser2.repository.UsersRepository
import com.masykur.githubuser2.ui.fragment.FollowingFragmentFav
import kotlinx.android.synthetic.main.activity_detail.*


class FavoriteDetail : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    companion object {
        const val USER_DETAIL = "user_detail"
        val followingFragmentFav = FollowingFragmentFav()
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_detail)
        val person = intent.getSerializableExtra(USER_DETAIL) as Item
        val viewModelProviderFactory = GithubViewModelProviderFactory(
            application,
            UsersRepository(UserDetail(this)), this
        )
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(GithubViewModel::class.java)

        contentLoad()

        val sectionsPagerAdapter = SectionsPagerAdapterFavorite(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.hide()
        ivBack.setOnClickListener {
            val moveIntent = Intent(this, FavoriteActivity::class.java)
            startActivity(moveIntent)
        }
        viewModel.getDetailUsers(person.login)
        tvNama.text = person.login
        tvFollowersAngka.text = person.followers.toString()
        tvFollowingAngka.text = person.following.toString()
        tvRepoAngka.text = person.public_repos.toString()
        tvCompanyCountry.text = "${person.company},${person.location}"
        circleImageView.load(person.avatar_url)


        loadFinish()



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