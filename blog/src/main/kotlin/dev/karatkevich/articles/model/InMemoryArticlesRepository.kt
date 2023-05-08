package dev.karatkevich.articles.model

import dev.karatkevich.articles.domain.ArticlesRepository
import dev.karatkevich.articles.domain.entities.Article
import dev.karatkevich.articles.domain.entities.Id
import dev.karatkevich.articles.domain.entities.Id.Companion.toId
import dev.karatkevich.articles.domain.entities.isEmpty
import java.util.UUID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

internal class InMemoryArticlesRepository(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
    private val idGenerator: () -> String = { UUID.randomUUID().toString() },
) : ArticlesRepository {

    private val dispatcher = dispatcher.limitedParallelism(1)

    private val articles = mutableListOf<Article>()

    override suspend fun getAll(): List<Article> {
        return withContext(dispatcher) {
            articles.toList()
        }
    }

    override suspend fun getById(id: Id): Article? {
        return withContext(dispatcher) {
            articles.find { it.id == id }
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
        return withContext(dispatcher) {
            val timestamp = clock.now()
            val newArticle = article.copy(
                id = idGenerator().toId(),
                publishDate = timestamp,
                updateDate = timestamp,
            )

            articles += newArticle

            newArticle
        }
    }

    private suspend fun update(article: Article): Article {
        return withContext(dispatcher) {
            val existingArticle = articles.find { it.id == article.id }

            val updatedArticle = existingArticle?.copy(
                title = article.title,
                description = article.description,
                cover = article.cover,
                updateDate = clock.now(),
            ) ?: article

            if (existingArticle != null) {
                articles.remove(existingArticle)
            }

            articles += updatedArticle

            updatedArticle
        }
    }

    override suspend fun delete(id: Id): Article? {
        val article = getById(id) ?: return null

        return withContext(dispatcher) {
            val isRemoved = articles.remove(article)

            if (isRemoved) {
                article
            } else {
                null
            }
        }
    }
}
