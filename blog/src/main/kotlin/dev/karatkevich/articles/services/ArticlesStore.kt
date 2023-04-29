package dev.karatkevich.articles.services

import dev.karatkevich.articles.models.Article

internal interface ArticlesStore {

    fun getArticles(): List<Article.Existing>
    fun addArticle(article: Article.Existing)
    fun replaceArticle(article: Article.Existing): Boolean
    fun removeArticle(id: String): Boolean

    companion object InMemory : ArticlesStore {
        private val articles = mutableListOf<Article.Existing>()

        override fun getArticles(): List<Article.Existing> = articles

        override fun addArticle(article: Article.Existing) {
            articles += article
        }

        override fun replaceArticle(article: Article.Existing): Boolean {
            val isRemoved = articles.removeIf { it.id == article.id }

            articles += article

            return isRemoved
        }

        override fun removeArticle(id: String): Boolean = articles.removeIf { it.id == id }
    }
}

internal fun ArticlesStore.find(predicate: (Article.Existing) -> Boolean) =
    getArticles().find(predicate)
