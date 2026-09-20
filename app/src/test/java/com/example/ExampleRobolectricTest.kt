package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ai.AutonomousBlueprints
import com.example.data.local.AppDatabase
import com.example.data.model.ProjectEntity
import com.example.data.repository.OmniDevRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: OmniDevRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = OmniDevRepository(database)
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `verify app name resource matches platform identity`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Codiffera", appName)
    }

    @Test
    fun `verify autonomous blueprints synthesis across languages`() {
        val rustProject = AutonomousBlueprints.synthesizeProject(
            prompt = "Ultra-fast microservice",
            targetPlatform = "Backend",
            targetLanguage = "Rust",
            targetFramework = "Axum + Tokio",
            targetStyle = "Cyber Dark"
        )
        assertNotNull(rustProject)
        assertEquals("Rust", rustProject.project.language)
        assertTrue(rustProject.files.isNotEmpty())
        assertTrue(rustProject.audits.isNotEmpty())

        val mobileProject = AutonomousBlueprints.synthesizeProject(
            prompt = "Crypto Vault",
            targetPlatform = "Mobile",
            targetLanguage = "Kotlin",
            targetFramework = "Jetpack Compose",
            targetStyle = "Modern Minimalist"
        )
        assertEquals("Kotlin", mobileProject.project.language)
        assertTrue(mobileProject.project.securityScore >= 95)
    }

    @Test
    fun `verify room database persistence for full project`() = runBlocking {
        val sample = AutonomousBlueprints.createDefaultProjects().first()
        val savedId = repository.saveFullProject(sample.project, sample.files, sample.audits)
        assertTrue(savedId > 0)

        val retrieved = repository.getFullProject(savedId)
        assertNotNull(retrieved)
        assertEquals(sample.project.title, retrieved!!.project.title)
        assertEquals(sample.files.size, retrieved.files.size)
        assertEquals(sample.audits.size, retrieved.audits.size)

        // Test favorite toggle
        repository.toggleFavorite(savedId, true)
        val updated = repository.getProjectById(savedId)
        assertTrue(updated!!.isFavorite)

        // Test list flow
        val allProjects = repository.getAllProjects().first()
        assertEquals(1, allProjects.size)
    }
}
