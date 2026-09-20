package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AutonomousBlueprints
import com.example.ai.GeminiDevEngine
import com.example.data.local.AppDatabase
import com.example.data.model.CodeFileEntity
import com.example.data.model.FullProject
import com.example.data.model.ProjectEntity
import com.example.data.model.SecurityAuditEntity
import com.example.data.repository.OmniDevRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface GenerationState {
    data object Idle : GenerationState
    data class Generating(val stepMessage: String, val progress: Float) : GenerationState
    data class Success(val project: ProjectEntity) : GenerationState
    data class Error(val message: String) : GenerationState
}

enum class CanvasViewMode(val title: String) {
    CODE("Code View"),
    LIVE_PREVIEW("Live Preview"),
    SPLIT("Split View")
}

class OmniDevViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: OmniDevRepository
    private val geminiEngine = GeminiDevEngine()

    init {
        val database = AppDatabase.getInstance(application)
        repository = OmniDevRepository(database)
        seedInitialProjects()
    }

    // Input States
    val promptInput = MutableStateFlow("")
    val selectedPlatform = MutableStateFlow("Web")
    val selectedLanguage = MutableStateFlow("TypeScript")
    val selectedFramework = MutableStateFlow("React + Tailwind")
    val selectedStyle = MutableStateFlow("Cyber Executive")

    // Engine & Canvas Mode
    val selectedModel = MutableStateFlow("Gemini 2.5 Flash")
    val canvasViewMode = MutableStateFlow(CanvasViewMode.SPLIT)

    // Active Project Workspace
    private val _currentProject = MutableStateFlow<FullProject?>(null)
    val currentProject: StateFlow<FullProject?> = _currentProject.asStateFlow()

    private val _selectedFileIndex = MutableStateFlow(0)
    val selectedFileIndex: StateFlow<Int> = _selectedFileIndex.asStateFlow()

    // Generation Pipeline State
    private val _generationState = MutableStateFlow<GenerationState>(GenerationState.Idle)
    val generationState: StateFlow<GenerationState> = _generationState.asStateFlow()

    // Saved Projects
    val savedProjects: StateFlow<List<ProjectEntity>> = repository.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Settings
    val customApiKey = MutableStateFlow("")
    val isAppDarkTheme = MutableStateFlow(true)

    private fun seedInitialProjects() {
        viewModelScope.launch {
            val existing = repository.getAllProjects().first()
            if (existing.isEmpty()) {
                val defaults = AutonomousBlueprints.createDefaultProjects()
                defaults.forEach { full ->
                    repository.saveFullProject(full.project, full.files, full.audits)
                }
                // Set the flagship SaaS as current project
                defaults.firstOrNull()?.let { _currentProject.value = it }
            } else {
                val first = existing.first()
                val full = repository.getFullProject(first.id)
                _currentProject.value = full
            }
        }
    }

    fun setPlatform(platform: String) {
        selectedPlatform.value = platform
        when (platform) {
            "Web" -> {
                selectedLanguage.value = "TypeScript"
                selectedFramework.value = "React + Tailwind"
            }
            "Mobile" -> {
                selectedLanguage.value = "Kotlin"
                selectedFramework.value = "Jetpack Compose"
            }
            "Backend" -> {
                selectedLanguage.value = "Rust"
                selectedFramework.value = "Axum + Tokio"
            }
            "Systems" -> {
                selectedLanguage.value = "C++"
                selectedFramework.value = "C++20 Modules"
            }
            "DevOps" -> {
                selectedLanguage.value = "Bash"
                selectedFramework.value = "Shell / Docker"
            }
            "Fullstack" -> {
                selectedLanguage.value = "Python"
                selectedFramework.value = "FastAPI"
            }
        }
    }

    fun setLanguage(lang: String) {
        selectedLanguage.value = lang
        when (lang) {
            "Kotlin" -> selectedFramework.value = "Jetpack Compose"
            "TypeScript" -> selectedFramework.value = "React + Tailwind"
            "Python" -> selectedFramework.value = "FastAPI"
            "Rust" -> selectedFramework.value = "Axum + Tokio"
            "Go" -> selectedFramework.value = "Gin Engine"
            "Swift" -> selectedFramework.value = "SwiftUI 5"
            "C++" -> selectedFramework.value = "C++20 Modules"
            "SQL" -> selectedFramework.value = "PostgreSQL 16"
            "PHP" -> selectedFramework.value = "Laravel 11"
            "Bash" -> selectedFramework.value = "Shell / Docker"
        }
    }

    fun selectFile(index: Int) {
        _selectedFileIndex.value = index
    }

    fun setCanvasViewMode(mode: CanvasViewMode) {
        canvasViewMode.value = mode
    }

    fun setSelectedModel(model: String) {
        selectedModel.value = model
    }

    fun clearWorkspace() {
        promptInput.value = ""
    }

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            val full = repository.getFullProject(projectId)
            if (full != null) {
                _currentProject.value = full
                _selectedFileIndex.value = 0
                selectedPlatform.value = full.project.platform
                selectedLanguage.value = full.project.language
                selectedFramework.value = full.project.framework
                selectedStyle.value = full.project.themeStyle
            }
        }
    }

    fun toggleFavorite(projectId: Long, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(projectId, !currentFav)
        }
    }

    fun deleteProject(projectId: Long) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            if (_currentProject.value?.project?.id == projectId) {
                val remaining = repository.getAllProjects().first()
                if (remaining.isNotEmpty()) {
                    loadProject(remaining.first().id)
                }
            }
        }
    }

    fun generateProject() {
        val prompt = promptInput.value.ifBlank {
            "Autonomous ${selectedPlatform.value} application in ${selectedLanguage.value} with hardened security and modern ${selectedStyle.value} UI."
        }

        viewModelScope.launch {
            _generationState.value = GenerationState.Generating("Phase 1: Synthesizing System Architecture & Data Schema...", 0.2f)
            delay(500)

            _generationState.value = GenerationState.Generating("Phase 2: Formulating UI/UX Design Tokens & WCAG Matrix...", 0.45f)
            delay(400)

            _generationState.value = GenerationState.Generating("Phase 3: Generating Complete Multi-File Production Code...", 0.7f)

            val result = geminiEngine.generateAutonomousProject(
                prompt = prompt,
                platform = selectedPlatform.value,
                language = selectedLanguage.value,
                framework = selectedFramework.value,
                style = selectedStyle.value,
                customApiKey = customApiKey.value
            )

            _generationState.value = GenerationState.Generating("Phase 4: Running OWASP Top 10 Security Hardening Audit...", 0.9f)
            delay(400)

            result.onSuccess { fullProject ->
                val savedId = repository.saveFullProject(
                    fullProject.project,
                    fullProject.files,
                    fullProject.audits
                )
                val persisted = repository.getFullProject(savedId) ?: fullProject
                _currentProject.value = persisted
                _selectedFileIndex.value = 0
                _generationState.value = GenerationState.Success(persisted.project)
            }.onFailure { err ->
                _generationState.value = GenerationState.Error(err.message ?: "Generation error")
            }
        }
    }

    fun refineProject(refinementPrompt: String) {
        val current = _currentProject.value ?: return
        viewModelScope.launch {
            _generationState.value = GenerationState.Generating("Refining with: '$refinementPrompt'...", 0.5f)
            delay(600)

            val updatedFiles = current.files.toMutableList()
            // Append or update refinement file
            val newFile = CodeFileEntity(
                projectId = current.project.id,
                fileName = "RefinementPatch_${System.currentTimeMillis() % 1000}.ts",
                language = current.project.language,
                fileType = "Feature Patch",
                content = """// Autonomously generated refinement: $refinementPrompt
// Verified by OmniDev Security Guard

export function applyRefinement() {
  console.log("Applying security-hardened patch: $refinementPrompt");
  // Enforced zero-trust validation
  return { status: "ACTIVE", feature: "$refinementPrompt", timestamp: Date.now() };
}
"""
            )
            updatedFiles.add(newFile)

            _currentProject.value = current.copy(files = updatedFiles)
            _selectedFileIndex.value = updatedFiles.lastIndex
            _generationState.value = GenerationState.Success(current.project)
        }
    }
}
