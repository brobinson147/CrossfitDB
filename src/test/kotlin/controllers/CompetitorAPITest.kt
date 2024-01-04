package controllers

import models.Compete
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import persistence.JSONSerializer
import java.io.File

class CompetitorAPITest {

    private var crossfitCompetition: Compete? = null
    private var swimmingCompetition: Compete? = null
    private var codingCompetition: Compete? = null
    private var testCompetition: Compete? = null
    private var runningCompetition: Compete? = null
    private var populatedCompetitions: CompetitorAPI? = CompetitorAPI(JSONSerializer(File("competitions.json")))
    private var emptyCompetitions: CompetitorAPI? = CompetitorAPI(JSONSerializer(File("competitions.json")))

    @BeforeEach
    fun setup() {
        crossfitCompetition = Compete("2023-01-15", "Crossfit Open", "Sport", "City Arena", 5, 3, null)
        swimmingCompetition = Compete("2023-02-20", "Swimming Challenge", "Sport", "Aquatic Center", 8, 8, null)
        codingCompetition = Compete("2023-03-10", "CodeFest", "Tech", "Tech Hub", 10, 10, null)
        testCompetition = Compete("2023-04-05", "Testing Championship", "Tech", "Test Arena", 12, 10, null)
        runningCompetition = Compete("2023-05-01", "Marathon Run", "Sport", "City Streets", 15, 15, null)

        // Adding 5 competitions to the competitions API
        populatedCompetitions!!.addCompetition(crossfitCompetition!!)
        populatedCompetitions!!.addCompetition(swimmingCompetition!!)
        populatedCompetitions!!.addCompetition(codingCompetition!!)
        populatedCompetitions!!.addCompetition(testCompetition!!)
        populatedCompetitions!!.addCompetition(runningCompetition!!)
    }

    @AfterEach
    fun tearDown() {
        crossfitCompetition = null
        swimmingCompetition = null
        codingCompetition = null
        testCompetition = null
        runningCompetition = null
        populatedCompetitions = null
        emptyCompetitions = null
    }

    @Nested
    inner class AddCompetitions {
        @Test
        fun `adding a competition to a populated list adds to ArrayList`() {
            val newCompetition =
                Compete("2023-06-01", "New Challenge", "Sport", "New Arena", 5, 3, null)
            assertEquals(5, populatedCompetitions!!.numberOfActiveCompetitions())
            assertTrue(populatedCompetitions!!.addCompetition(newCompetition))
            assertEquals(6, populatedCompetitions!!.numberOfActiveCompetitions())
            assertEquals(
                newCompetition,
                populatedCompetitions!!.getCompetition(populatedCompetitions!!.numberOfActiveCompetitions() - 1)
            )
        }

        @Test
        fun `adding a competition to an empty list adds to ArrayList`() {
            val newCompetition =
                Compete("2023-06-01", "New Challenge", "Sport", "New Arena", 5, 3, null)
            assertEquals(0, emptyCompetitions!!.numberOfActiveCompetitions())
            assertTrue(emptyCompetitions!!.addCompetition(newCompetition))
            assertEquals(1, emptyCompetitions!!.numberOfActiveCompetitions())
            assertEquals(
                newCompetition,
                emptyCompetitions!!.getCompetition(emptyCompetitions!!.numberOfActiveCompetitions() - 1)
            )
        }
    }

    @Nested
    inner class ListCompetitions {

        @Test
        fun `listAllCompetitions returns No Competitions Stored message when ArrayList is empty`() {
            assertEquals(0, emptyCompetitions!!.numberOfActiveCompetitions())
            assertTrue(emptyCompetitions!!.listAllCompetitions().lowercase().contains("no competitions"))
        }

        @Test
        fun `listAllCompetitions returns Competitions when ArrayList has competitions stored`() {
            assertEquals(5, populatedCompetitions!!.numberOfActiveCompetitions())
            val competitionsString = populatedCompetitions!!.listAllCompetitions().lowercase()
            assertTrue(competitionsString.contains("crossfit open"))
            assertTrue(competitionsString.contains("codefest"))
            assertTrue(competitionsString.contains("testing championship"))
            assertTrue(competitionsString.contains("swimming challenge"))
            assertTrue(competitionsString.contains("marathon run"))
        }

        @Test
        fun `listActiveCompetitions returns no active competitions stored when ArrayList is empty`() {
            assertEquals(0, emptyCompetitions!!.numberOfActiveCompetitions())
            assertTrue(
                emptyCompetitions!!.listActiveCompetitions().lowercase().contains("no active competitions")
            )
        }

        @Test
        fun `listActiveCompetitions returns active competitions when ArrayList has active competitions stored`() {
            assertEquals(3, populatedCompetitions!!.numberOfActiveCompetitions())
            val activeCompetitionsString = populatedCompetitions!!.listActiveCompetitions().lowercase()
            assertTrue(activeCompetitionsString.contains("crossfit open"))
            assertFalse(activeCompetitionsString.contains("codefest"))
            assertTrue(activeCompetitionsString.contains("testing championship"))
            assertTrue(activeCompetitionsString.contains("swimming challenge"))
            assertFalse(activeCompetitionsString.contains("marathon run"))
        }



        @Nested
        inner class DeleteCompetitions {

            @Test
            fun `deleting a competition that does not exist, returns null`() {
                assertNull(emptyCompetitions!!.deleteCompetition(0))
                assertNull(populatedCompetitions!!.deleteCompetition(-1))
                assertNull(populatedCompetitions!!.deleteCompetition(5))
            }

            @Test
            fun `deleting a competition that exists delete and returns deleted object`() {
                assertEquals(5, populatedCompetitions!!.numberOfActiveCompetitions())
                assertEquals(runningCompetition, populatedCompetitions!!.deleteCompetition(4))
                assertEquals(4, populatedCompetitions!!.numberOfActiveCompetitions())
                assertEquals(crossfitCompetition, populatedCompetitions!!.deleteCompetition(0))
                assertEquals(3, populatedCompetitions!!.numberOfActiveCompetitions())
            }
        }

        @Nested
        inner class UpdateCompetitions {
            @Test
            fun `updating a competition that does not exist returns false`() {
                assertFalse(
                    populatedCompetitions!!.updateCompetition(
                        6,
                        Compete("2023-06-01", "Updated Challenge", "Sport", "Updated Arena", 5, 3, null)
                    )
                )
                assertFalse(
                    populatedCompetitions!!.updateCompetition(
                        -1,
                        Compete("2023-06-01", "Updated Challenge", "Sport", "Updated Arena", 5, 3, null)
                    )
                )
                assertFalse(
                    emptyCompetitions!!.updateCompetition(
                        0,
                        Compete("2023-06-01", "Updated Challenge", "Sport", "Updated Arena", 5, 3, null)
                    )
                )
            }

            @Test
            fun `updating a competition that exists returns true and updates`() {
                // Check competition 5 exists and check the contents
                assertEquals(runningCompetition, populatedCompetitions!!.getCompetition(4))
                assertEquals("Marathon Run", populatedCompetitions!!.getCompetition(4)!!.competitionTitle)
                assertEquals(15, populatedCompetitions!!.getCompetition(4)!!.numberOfWorkoutAttempted)
                assertEquals("Sport", populatedCompetitions!!.getCompetition(4)!!.competitionCategory)

                // Update competition 5 with new information and ensure contents updated successfully
                assertTrue(
                    populatedCompetitions!!.updateCompetition(
                        4,
                        Compete("2023-06-01", "Updated Challenge", "Hobby", "Updated Arena", 10, 5, null)
                    )
                )
                assertEquals("Updated Challenge", populatedCompetitions!!.getCompetition(4)!!.competitionTitle)
                assertEquals(10, populatedCompetitions!!.getCompetition(4)!!.numberOfWorkoutAttempted)
                assertEquals("Hobby", populatedCompetitions!!.getCompetition(4)!!.competitionCategory)
            }
        }

        @Nested
        inner class ArchiveCompetitions {
            @Test
            fun `archiving a competition that does not exist returns false`() {
                assertFalse(populatedCompetitions!!.archiveCompetition(6))
                assertFalse(populatedCompetitions!!.archiveCompetition(-1))
                assertFalse(emptyCompetitions!!.archiveCompetition(0))
            }

            @Test
            fun `archiving an already archived competition returns false`() {
                assertTrue(populatedCompetitions!!.getCompetition(2)!!.isCompetitionArchived)
                assertFalse(populatedCompetitions!!.archiveCompetition(2))
            }

            @Test
            fun `archiving an active competition that exists returns true and archives`() {
                assertFalse(populatedCompetitions!!.getCompetition(1)!!.isCompetitionArchived)
                assertTrue(populatedCompetitions!!.archiveCompetition(1))
                assertTrue(populatedCompetitions!!.getCompetition(1)!!.isCompetitionArchived)
            }
        }
    }
}
