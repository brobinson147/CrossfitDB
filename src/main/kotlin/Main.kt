// Main.kt
import mu.KotlinLogging
import persistence.JSONSerializer
import java.io.File
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.ValidateInput.readValidCategory
import utils.ValidateInput.readValidPriority
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val competitorAPI = CompetitorAPI(JSONSerializer(File("competitors.json")))

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

}

fun addCompetition() {

}

fun listCompetitions() {

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

// Add the utility function lastCompetitorId() here

