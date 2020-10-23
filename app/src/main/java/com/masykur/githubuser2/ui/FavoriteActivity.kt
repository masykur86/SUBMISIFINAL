package com.masykur.githubuser2.ui

import android.app.ProgressDialog.show
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.util.Log
import android.view.SoundEffectConstants.CLICK
import android.view.View
import android.widget.Toast
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.masykur.githubuser2.ContentProvider.Companion.URI_USER
import com.masykur.githubuser2.R
import com.masykur.githubuser2.adapter.UserAdapter
import com.masykur.githubuser2.db.UserDetail
import com.masykur.githubuser2.helper.MappingHelper
import com.masykur.githubuser2.models.Item
import com.masykur.githubuser2.repository.UsersRepository
import com.masykur.githubuser2.ui.fragment.FollowingFragmentFav
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    lateinit var viewModel: GithubViewModel
    lateinit var userAdapter: UserAdapter
     lateinit var view: View

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
//        supportActionBar?.hide()
        val viewModelProviderFactory = GithubViewModelProviderFactory(
            application,
            UsersRepository(UserDetail(this)),
            this
        )
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(GithubViewModel::class.java)
        userAdapter = UserAdapter()
        setupRV()
        loadData()



//        viewModel.getSavedUser().observe(this, Observer {
//            userAdapter.differ.submitList(it)
//        })
        userAdapter.setOnItemCLickListener {
            val moveWithObjectIntent = Intent(this@FavoriteActivity, FavoriteDetail::class.java)
            moveWithObjectIntent.putExtra(FavoriteDetail.USER_DETAIL, it)
            startActivity(moveWithObjectIntent)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val userDetail = userAdapter.differ.currentList[position]
                viewModel.deleteUser(userDetail)
                Snackbar.make(rvUsersFavorite,"user dihapus",Snackbar.LENGTH_LONG).apply {
                    show()
                }

            }

        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(rvUsersFavorite)
        }
    }



    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main){
            val deferredNotes = async(Dispatchers.IO){
                val cursor = contentResolver?.query(URI_USER, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferredNotes.await()
            userAdapter.differ.submitList(user)

        }
    }

    private fun setupRV() {
        userAdapter = UserAdapter()
        rvUsersFavorite.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
        }
    }
}