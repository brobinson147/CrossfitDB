package controllers

import models.Compete
import models.Competitor
import persistence.Serializer

class CompetitorAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var competitors = ArrayList<Competitor>()
    private var competitions = ArrayList<Compete>()

    fun formatListString(itemsToFormat: List<Any>): String =
        itemsToFormat.joinToString(separator = "\n") { item ->
            // Implement the logic to format each item as a string
            item.toString()
        }

    fun addCompetitor(competitor: Competitor): Boolean {
        return competitors.add(competitor)
    }

    fun deleteCompetition(index: Int): Compete? {
        return if (isValidCompetitionIndex(index)) {
            competitions.removeAt(index)
        } else {
            null
        }
    }

    fun addCompetition(competition: Compete): Boolean {
        return competitions.add(competition)
    }


    fun listActiveCompetitions(): String =
        if (numberOfActiveCompetitions() == 0) "No active competitions stored"
        else formatListString(competitions.filter { competition -> !competition.isCompetitionArchived })

    fun numberOfActiveCompetitions(): Int = competitions.count { competition: Compete -> !competition.isCompetitionArchived }

    fun archiveCompetition(indexToArchive: Int): Boolean {
        if (isValidCompetitionIndex(indexToArchive)) {
            val competitionToArchive = competitions[indexToArchive]
            if (!competitionToArchive.isCompetitionArchived) {
                competitionToArchive.isCompetitionArchived = true
                return true
            }
        }
        return false
    }

    fun updateCompetition(indexToUpdate: Int, competition: Compete): Boolean {
        if (isValidCompetitionIndex(indexToUpdate)) {
            competitions[indexToUpdate] = competition
            return true
        }
        return false
    }


    fun isValidCompetitionIndex(index: Int): Boolean {
        return (index >= 0 && index < competitions.size)
    }

    fun getCompetitorById(id: Int): Competitor? {
        return competitors.firstOrNull { it.id == id }
    }

    fun numberOfCompetitors(): Int {
        return competitors.size
    }

    fun searchCompetitorByName(name: String): List<Competitor> {
        val searchResults = competitors.filter { competitor -> competitor.name.equals(name, ignoreCase = true) }
        return searchResults.toList()
    }

    fun getAllCompetitions(): List<Compete> {
        return competitions.toList()
    }

    fun getAllCompetitors(): List<Competitor> {
        return competitors.toList()
    }

    fun getCompetition(index: Int): Compete? {
        return if (isValidCompetitionIndex(index)) {
            competitions[index]
        } else {
            null
        }
    }

    @Throws(Exception::class)
    fun load() {
        // Load both competitors and competitions from the serializer
        val loadedData = serializer.read() as Pair<ArrayList<Competitor>, ArrayList<Compete>>
        competitors = loadedData.first
        competitions = loadedData.second
    }

    @Throws(Exception::class)
    fun store() {
        // Store both competitors and competitions using the serializer
        serializer.write(Pair(competitors, competitions))
    }
}