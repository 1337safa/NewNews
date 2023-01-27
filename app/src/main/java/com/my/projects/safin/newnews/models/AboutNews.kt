package com.my.projects.safin.newnews.models

data class AboutNews(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)