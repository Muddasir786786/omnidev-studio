package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val prompt: String,
    val platform: String, // "Web", "Mobile", "Backend", "Fullstack", "DevOps"
    val language: String, // "Kotlin", "TypeScript", "Python", "Rust", "Go", "Swift", "C++", "SQL", "PHP", "Bash"
    val framework: String, // "Jetpack Compose", "React + Tailwind", "FastAPI", "Axum", "Next.js", "Flutter", etc.
    val themeStyle: String, // "Modern Minimalist", "Cyber Dark", "Glassmorphic", "Bento Grid"
    val securityScore: Int, // 0 - 100
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val architectureSummary: String = "",
    val designTokensJson: String = ""
)

@Entity(tableName = "code_files")
data class CodeFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val fileName: String,
    val language: String,
    val fileType: String, // "UI / View", "Business Logic", "Data Model", "Security Middleware", "Database Migration", "Config"
    val content: String,
    val isPrimary: Boolean = false
)

@Entity(tableName = "security_audits")
data class SecurityAuditEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val projectId: Long,
    val category: String, // "OWASP Top 10", "Input Validation", "Cryptographic Failures", "Auth & Access", "Network & Headers"
    val severity: String, // "CRITICAL", "HIGH", "MEDIUM", "LOW", "RESOLVED"
    val title: String,
    val details: String,
    val mitigationCode: String,
    val isMitigated: Boolean = true
)

data class FullProject(
    val project: ProjectEntity,
    val files: List<CodeFileEntity>,
    val audits: List<SecurityAuditEntity>
)
