package dev.karatkevich.articles.model

import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id
import dev.karatkevich.articles.domain.entities.isEmpty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class InMemoryArticlesRepository(
    private val dispatcher: CoroutineDispatcher,
) : ArticlesRepository {

    private val articles = mutableListOf<Article>()
    private val mutex = Mutex()

    override suspend fun getAll(): List<Article> {
        return mutex.withLock {
            withContext(dispatcher) {
                articles.toList()
            }
        }
    }

    override suspend fun getById(id: Id): Article? {
        return mutex.withLock {
            withContext(dispatcher) {
                articles.find { it.id == id }
            }
        }
    }

    override suspend fun save(article: Article): Article {
        return if (article.id.isEmpty()) {
            create(article)
        } else {
            update(article)
        }
    }

    private suspend fun create(article: Article): Article {
        return mutex.withLock {
            withContext(dispatcher) {
                articles += article
                article
            }
        }
    }

    private suspend fun update(article: Article): Article {
        return mutex.withLock {
            withContext(dispatcher) {
                val existingArticle = articles.find { it.id == article.id }

                val updatedArticle = existingArticle?.copy(
                    title = article.title,
                    description = article.description,
                    cover = article.cover
                ) ?: article

                if (existingArticle != null) {
                    articles.remove(existingArticle)
                }

                articles += updatedArticle

                updatedArticle
            }
        }
    }

    override suspend fun delete(id: Id): Article? {
        val article = getById(id) ?: return null

        return mutex.withLock {
            withContext(dispatcher) {
                val isRemoved = articles.remove(article)

                if (isRemoved) {
                    article
                } else {
                    null
                }
            }
        }
    }
}
