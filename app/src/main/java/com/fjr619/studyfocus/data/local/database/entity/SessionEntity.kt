package com.fjr619.studyfocus.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SessionEntity(
    val relatedToSubject: String,
    val date: Long,
    val duration: Long,
    val sessionSubjectId: Int,
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int? = null
)