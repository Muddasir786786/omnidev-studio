package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.CodeFileEntity
import com.example.data.model.FullProject
import com.example.data.model.ProjectEntity
import com.example.data.model.SecurityAuditEntity
import kotlinx.coroutines.flow.Flow

class OmniDevRepository(private val database: AppDatabase) {
    private val projectDao = database.projectDao()
    private val codeFileDao = database.codeFileDao()
    private val securityAuditDao = database.securityAuditDao()

    fun getAllProjects(): Flow<List<ProjectEntity>> = projectDao.getAllProjects()

    suspend fun getProjectById(id: Long): ProjectEntity? = projectDao.getProjectById(id)

    fun getFilesForProject(projectId: Long): Flow<List<CodeFileEntity>> =
        codeFileDao.getFilesForProject(projectId)

    fun getAuditsForProject(projectId: Long): Flow<List<SecurityAuditEntity>> =
        securityAuditDao.getAuditsForProject(projectId)

    suspend fun getFullProject(projectId: Long): FullProject? {
        val project = projectDao.getProjectById(projectId) ?: return null
        val files = codeFileDao.getFilesForProjectSync(projectId)
        val audits = securityAuditDao.getAuditsForProjectSync(projectId)
        return FullProject(project, files, audits)
    }

    suspend fun saveFullProject(
        project: ProjectEntity,
        files: List<CodeFileEntity>,
        audits: List<SecurityAuditEntity>
    ): Long {
        val newProjectId = projectDao.insertProject(project)
        val filesWithId = files.map { it.copy(projectId = newProjectId) }
        val auditsWithId = audits.map { it.copy(projectId = newProjectId) }
        codeFileDao.insertFiles(filesWithId)
        securityAuditDao.insertAudits(auditsWithId)
        return newProjectId
    }

    suspend fun deleteProject(projectId: Long) {
        codeFileDao.deleteFilesForProject(projectId)
        securityAuditDao.deleteAuditsForProject(projectId)
        projectDao.deleteProjectById(projectId)
    }

    suspend fun toggleFavorite(projectId: Long, isFav: Boolean) {
        projectDao.toggleFavorite(projectId, isFav)
    }
}
