package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CodeFileEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SecurityAuditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY createdAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("UPDATE projects SET isFavorite = :isFav WHERE id = :id")
    suspend fun toggleFavorite(id: Long, isFav: Boolean)
}

@Dao
interface CodeFileDao {
    @Query("SELECT * FROM code_files WHERE projectId = :projectId ORDER BY isPrimary DESC, fileName ASC")
    fun getFilesForProject(projectId: Long): Flow<List<CodeFileEntity>>

    @Query("SELECT * FROM code_files WHERE projectId = :projectId ORDER BY isPrimary DESC, fileName ASC")
    suspend fun getFilesForProjectSync(projectId: Long): List<CodeFileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFiles(files: List<CodeFileEntity>)

    @Query("DELETE FROM code_files WHERE projectId = :projectId")
    suspend fun deleteFilesForProject(projectId: Long)
}

@Dao
interface SecurityAuditDao {
    @Query("SELECT * FROM security_audits WHERE projectId = :projectId ORDER BY id ASC")
    fun getAuditsForProject(projectId: Long): Flow<List<SecurityAuditEntity>>

    @Query("SELECT * FROM security_audits WHERE projectId = :projectId ORDER BY id ASC")
    suspend fun getAuditsForProjectSync(projectId: Long): List<SecurityAuditEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudits(audits: List<SecurityAuditEntity>)

    @Query("DELETE FROM security_audits WHERE projectId = :projectId")
    suspend fun deleteAuditsForProject(projectId: Long)
}
