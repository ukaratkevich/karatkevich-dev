package dev.karatkevich.di

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.karatkevich.Database
import java.util.Properties

object DatabaseModule {
    val database = Database(
        JdbcSqliteDriver(
            url = JdbcSqliteDriver.IN_MEMORY,
            properties = Properties().apply { put("foreign_keys", "true") }
        ),
    )
}
