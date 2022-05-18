package com.example.kakaostyle.dao

import com.example.kakaostyle.model.UserVacation
import com.example.kakaostyle.model.UserVacationHistory
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface VacationHistoryDao : JpaRepository<UserVacationHistory, Long> {
    fun findOneById(id: Long): UserVacationHistory?
    fun findByUserVacation(userVacation: UserVacation, sort: Sort?): List<UserVacationHistory>

    @Query("SELECT uvh FROM UserVacationHistory uvh WHERE uvh.startAt BETWEEN :startAt AND :endAt OR uvh.endAt BETWEEN :startAt AND :endAt")
    fun hasDuplicateVacationHistory(startAt:LocalDateTime, endAt:LocalDateTime): List<UserVacationHistory>?


}