package com.masykur.githubuser2.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.masykur.githubuser2.R
import com.masykur.githubuser2.models.Item
import com.masykur.githubuser2.ui.GithubViewModel
import kotlinx.android.synthetic.main.user_list.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    //    mengecek apakah ada data yang sama atau tidak
    private val differCallback = object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val users = differ.currentList[position]
        holder.itemView.apply {
            this.tvNama.text = users.login
            this.ivAvatar.load(users.avatar_url)
            setOnClickListener {
                onItemClickListener?.let { it(users) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Item) -> Unit)? = null

    fun setOnItemCLickListener(listener: (Item) -> Unit) {
        onItemClickListener = listener
    }

}
