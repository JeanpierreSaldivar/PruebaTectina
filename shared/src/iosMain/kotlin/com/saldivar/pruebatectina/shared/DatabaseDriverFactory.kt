package com.saldivar.pruebatectina.shared

import com.saldivar.pruebatectina.shared.cache.AppDatabase
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "test.db")
    }
}