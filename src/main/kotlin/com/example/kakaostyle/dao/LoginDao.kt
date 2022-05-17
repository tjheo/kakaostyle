package com.example.kakaostyle.dao

import com.example.kakaostyle.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomLoginDao: JpaRepository<User, Long> {
    fun findOneByEmailAndPassword(email: String, password: String): User?

    fun findByAccessToken(access_token: String): User?

}