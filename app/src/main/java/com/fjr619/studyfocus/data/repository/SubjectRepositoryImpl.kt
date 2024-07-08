package com.fjr619.studyfocus.data.repository

import com.fjr619.studyfocus.data.local.database.SubjectDao
import com.fjr619.studyfocus.data.mapper.toSubject
import com.fjr619.studyfocus.data.mapper.toSubjectEntity
import com.fjr619.studyfocus.domain.model.Subject
import com.fjr619.studyfocus.domain.repository.SubjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectRepositoryImpl(
    private val subjectDao: SubjectDao,
) : SubjectRepository {
    override suspend fun upsertSubject(subject: Subject) {
        subjectDao.upsertSubject(subject.toSubjectEntity())
    }

    override fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    override fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    override suspend fun deleteSubject(subjectInt: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getSubjectById(subjectInt: Int): Subject? {
        TODO("Not yet implemented")
    }

    override fun getAllSubjects(): Flow<List<Subject>> {
        return subjectDao.getAllSubjects().map {
            it.map { subjectEntity -> subjectEntity.toSubject()
            }
        }
    }
}