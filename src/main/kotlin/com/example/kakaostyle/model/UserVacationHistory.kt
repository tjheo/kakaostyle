package com.example.kakaostyle.model
import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.NoArgsConstructor
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@NoArgsConstructor
@Table(name = "user_vacation_history")
class UserVacationHistory(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "user_vacation_id")
        @JsonBackReference
        var userVacation: UserVacation?,
        var startAt: LocalDateTime,
        var endAt: LocalDateTime,
        var createdAt: LocalDateTime = LocalDateTime.now(),
        var updateAt: LocalDateTime = LocalDateTime.now(),
        var reason: String,
        var status : String = "",
        var vacationDays: Double

        ) {
        fun getUserVacation(){

        }
}