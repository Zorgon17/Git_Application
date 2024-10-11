package com.example.gitapplication.pojomodel

object EventGenerator {
    fun generateEvents(count: Int): List<Repository2.RepositoryItem2> =
        (0..count).map { index ->
            Repository2.RepositoryItem2(
                name = "Событие $index",
                language = "Событие $index",
                description = "Событие $index"
            )
        }
}