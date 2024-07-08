package com.fjr619.studyfocus.data.repository

import com.fjr619.studyfocus.data.local.database.SessionDao
import com.fjr619.studyfocus.data.mapper.toSession
import com.fjr619.studyfocus.domain.model.Session
import com.fjr619.studyfocus.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class SessionRepositoryImpl(
    private val sessionDao: SessionDao
) : SessionRepository {

    override suspend fun insertSession(session: Session) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSession(session: Session) {
        TODO("Not yet implemented")
    }

    override fun getAllSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getRecentFiveSessions(): Flow<List<Session>> {
        return sessionDao.getAllSessions().take(count = 5)
            .map { it.map { sessionEntity -> sessionEntity.toSession() } }
    }

    override fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    override fun getTotalSessionsDurationBySubjectId(subjectId: Int): Flow<Long> {
        TODO("Not yet implemented")
    }
}