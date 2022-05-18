package com.example.kakaostyle.dao

import com.example.kakaostyle.model.User
import com.example.kakaostyle.model.UserVacation
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VacationDao : JpaRepository<UserVacation, Long> {
    fun findOneByUserAndId(user: User, id: Long): UserVacation?

    fun findByUserAndYear(user: User, year: Int): UserVacation?
    fun findByUser(user: User, sort: Sort?): List<UserVacation>?


}