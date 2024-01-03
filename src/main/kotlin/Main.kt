// Main.kt
import models.Compete
import mu.KotlinLogging
import persistence.JSONSerializer
import utils.CategoryUtility
import java.io.File
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.ValidateInput.readValidCategory
import utils.ValidateInput.readValidPriority
import java.lang.System.exit
import java.util.concurrent.atomic.AtomicInteger

private val logger = KotlinLogging.logger {}
private val competitorAPI = CompetitorAPI(JSONSerializer(File("competitors.json")))
private val competitorIdCounter = AtomicInteger(1)

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu(): Int {
    return readNextInt("""
         > -------------------------------------
         > |        CROSSFIT APP MAIN MENU      |
         > -------------------------------------
         > | MENU                               |
         > |   1) Add Competitor                |
         > |   2) List Competitors              |
         > |   3) Add Competition               |
         > |   4) List Competitions             |
         > -------------------------------------
         > |   20) Save Data                    |
         > |   21) Load Data                    |
         > -------------------------------------
         > |   0) Exit                          |
         > -------------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1 -> addCompetitor()
            2 -> listCompetitors()
            3 -> addCompetition()
            4 -> listCompetitions()
            20 -> saveData()
            21 -> loadData()
            0 -> exitApp()
            else -> println("Invalid option entered: ${option}")
        }
    } while (true)
}


fun addCompetitor() {
    val name = readNextLine("Enter competitor name: ")
    val affiliation = readNextLine("Enter competitor affiliation: ")
    val dob = readNextLine("Enter competitor date of birth: ")

    val competitor = Competitor(lastCompetitorId(), name, affiliation, dob)
    if (competitorAPI.addCompetitor(competitor)) {
        println("Competitor added successfully.")
    } else {
        println("Failed to add competitor.")
    }
}

fun listCompetitors() {
    val competitors = competitorAPI.getAllCompetitors()
    if (competitors.isEmpty()) {
        println("No competitors found.")
    } else {
        println("Competitors:")
        for ((index, competitor) in competitors.withIndex()) {
            println("$index: $competitor")
        }
    }
}

fun lastCompetitorId(): Int = competitorIdCounter.get()

fun addCompetition() {
    val date = readNextLine("Enter the date of the competition: ")
    val title = readNextLine("Enter the title of the competition: ")
    val category = readValidCategory("Enter the category of the competition from ${CategoryUtility.categories}: ")
    val place = readNextLine("Enter the place of the competition: ")
    val numberOfWorkoutAttempted = readNextInt("Enter the number of workouts attempted: ")
    val numberOfWorkoutsCompleted = readNextInt("Enter the number of workouts completed: ")

    val competitorId = readNextInt("Enter the ID of the competitor for this competition (use 0 for none): ")
    val competitor = competitorAPI.getCompetitorById(competitorId)

    val isAdded = competitorAPI.addCompetition(
        Compete(date, title, category, place, numberOfWorkoutAttempted, numberOfWorkoutsCompleted, competitor)
    )

    if (isAdded) {
        println("Competition Added Successfully")
    } else {
        println("Add Competition Failed")
    }

}

fun listCompetitions() {
    val competitions = competitorAPI.getAllCompetitions()
    if (competitions.isEmpty()) {
        println("No competitions found.")
    } else {
        println("Competitions:")
        for ((index, competition) in competitions.withIndex()) {
            println("$index: $competition")
        }
    }

}

fun saveData() {
    try {
        competitorAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun loadData() {
    try {
        competitorAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp() {
    logger.info { "Exiting Application" }
    exit(0)
}


