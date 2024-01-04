package models

data class Compete(
    var id: Int,
    var date: String,
    var title: String,
    var categoryAttempted: String,
    var place: String,
    var numberOfWorkoutAttempted: Int,
    var numberOfWorkoutsCompleted: Competitor?
)
{
    var isCompetitionArchived: Boolean = false
}