package dev.karatkevich

import io.ktor.resources.Resource

@Resource("/v1/blog")
class Blog {

    @Resource("articles")
    class Articles(val parent: Blog = Blog()) {

        @Resource("{id}")
        class Id(
            val parent: Articles = Articles(),
            val id: String,
        )
    }
}
