package com.example.kutumbassignment.dataClasses

data class Repository(
	val forks: Int? = null,
	val starsSince: Int? = null,
	val builtBy: List<BuiltByItem?>? = null,
	val totalStars: Int? = null,
	val rank: Int? = null,
	val description: String? = null,
	val language: String? = null,
	val languageColor: String? = null,
	val repositoryName: String? = null,
	val url: String? = null,
	val username: String? = null,
	val since: String? = null
)

data class BuiltByItem(
	val avatar: String? = null,
	val url: String? = null,
	val username: String? = null
)
