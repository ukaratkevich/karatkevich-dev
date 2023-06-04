package dev.karatkevich.blog.articles.domain

import dev.karatkevich.blog.articles.domain.entities.Article
import dev.karatkevich.blog.articles.domain.entities.Id

interface ArticlesRepository {
    suspend fun getAll(): List<Article>
    suspend fun getById(id: Id): Article?
    suspend fun save(article: Article): Article
    suspend fun delete(id: Id): Article?
}
