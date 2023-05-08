package dev.karatkevich.articles.domain

import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id

class ArticlesService(
    private val repository: ArticlesRepository,
) {

    suspend fun getAll(): List<Article> = repository.getAll()

    suspend fun getById(id: Id): Article? = repository.getById(id)

    suspend fun create(article: Article): Article = repository.save(article)

    suspend fun update(article: Article): Article? {
        val existingArticle = getById(article.id)

        if (existingArticle != null) {
            return repository.save(article)
        }

        return null
    }

    suspend fun delete(id: Id): Article? {
        return repository.delete(id)
    }
}
