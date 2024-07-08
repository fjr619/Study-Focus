package com.fjr619.studyfocus.data.local.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.fjr619.studyfocus.data.local.database.entity.SubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubjectDao {

    @Upsert
    suspend fun upsertSubject(subject: SubjectEntity)

    @Query("SELECT COUNT(*) FROM SubjectEntity")
    fun getTotalSubjectCount(): Flow<Int>

    @Query("SELECT SUM(goalHours) FROM SubjectEntity")
    fun getTotalGoalHours(): Flow<Float>

    @Query("SELECT * FROM SubjectEntity WHERE subjectId = :subjectId")
    suspend fun getSubjectById(subjectId: Int): SubjectEntity?

    @Query("DELETE FROM SubjectEntity WHERE subjectId = :subjectId")
    suspend fun deleteSubject(subjectId: Int)

    @Query("SELECT * FROM SubjectEntity")
    fun getAllSubjects(): Flow<List<SubjectEntity>>
}