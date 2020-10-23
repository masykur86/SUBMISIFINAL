package com.masykur.githubuser2.models

data class GithubData(
    val incomplete_results: Boolean,
    val items: MutableList<Item>,
    val total_count: Int
)