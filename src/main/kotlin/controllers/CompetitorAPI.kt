package controllers

import models.Competitor
import persistence.Serializer

class CompetitorAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var competitors = ArrayList<Competitor>()
    private var competitions = ArrayList<Competition>()

    fun addCompetitor(competitor: Competitor): Boolean {
        return competitors.add(competitor)
    }

    fun addCompetition(competition: Competition): Boolean {
        return competitions.add(competition)
    }

    // Existing methods...

    fun listActiveCompetitions(): String =
        if (numberOfActiveCompetitions() == 0) "No active competitions stored"
        else formatListString(competitions.filter { competition -> !competition.isCompetitionArchived })

    fun numberOfActiveCompetitions(): Int = competitions.count { competition: Competition -> !competition.isCompetitionArchived }

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

    private fun isValidCompetitionIndex(index: Int): Boolean {
        return (index >= 0 && index < competitions.size)
    }


    @Throws(Exception::class)
    fun load() {
        // Load both competitors and competitions from the serializer
        val loadedData = serializer.read() as Pair<ArrayList<Competitor>, ArrayList<Competition>>
        competitors = loadedData.first
        competitions = loadedData.second
    }

    @Throws(Exception::class)
    fun store() {
        // Store both competitors and competitions using the serializer
        serializer.write(Pair(competitors, competitions))
    }
}