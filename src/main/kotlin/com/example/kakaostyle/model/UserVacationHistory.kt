package com.example.kakaostyle.model
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_vacation_history")
class UserVacationHistory(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
        @ManyToOne val user_vacation: UserVacation,

        var createdAt: LocalDateTime,
        var updateAt: LocalDateTime,
        val vacation: Double) {
}