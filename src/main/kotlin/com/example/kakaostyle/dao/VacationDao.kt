package com.example.kakaostyle.dao

import com.example.kakaostyle.model.UserVacation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VacationDao : JpaRepository<UserVacation, Long> {

    @Query("SELECT e FROM UserVacation e")
    fun hasMultipleLastName(): List<UserVacation>

}