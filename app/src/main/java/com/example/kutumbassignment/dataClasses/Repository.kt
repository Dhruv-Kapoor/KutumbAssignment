package com.example.kutumbassignment.dataClasses

import androidx.room.Entity
import androidx.room.PrimaryKey

abstract class RepositoryListItem

data class RepositoryHeader(
	var languageName: String? = null,
	var languageColor: String? = null
): RepositoryListItem()

@Entity(tableName = "Repository")
data class Repository(
	var forks: Int? = null,
	var starsSince: Int? = null,
	var builtBy: List<BuiltByItem?>? = null,
	var totalStars: Int? = null,
	@PrimaryKey
	var rank: Int? = null,
	var description: String? = null,
	var language: String? = null,
	var languageColor: String? = null,
	var repositoryName: String? = null,
	var url: String? = null,
	var username: String? = null,
	var since: String? = null,
	var isFavorite: Boolean = false
): RepositoryListItem()

@Entity
data class BuiltByItem(
	var avatar: String? = null,
	var url: String? = null,
	var username: String? = null
)

