package com.fjr619.studyfocus.data.repository

import com.fjr619.studyfocus.data.local.database.SubjectDao
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow

class SubjectRepositoryImpl(
    private val subjectDao: SubjectDao,
) : SubjectRepository {
    override suspend fun upsertSubject(subject: Subject) {
        TODO("Not yet implemented")
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        TODO("Not yet implemented")
    }

    override fun getTotalGoalHours(): Flow<Float> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSubject(subjectInt: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectById(subjectInt: Int): Subject? {
        TODO("Not yet implemented")
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        TODO("Not yet implemented")
    }
}