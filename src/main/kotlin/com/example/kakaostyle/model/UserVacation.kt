package com.example.kakaostyle.model
import com.fasterxml.jackson.annotation.JsonIgnore
import lombok.NoArgsConstructor
import java.time.LocalDate
import javax.persistence.*

@Entity
@NoArgsConstructor
@Table(name = "user_vacation")
class UserVacation(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id : Long = 0,
        @JsonIgnore
        @ManyToOne
        val user : User? = null,
        val year : Int = LocalDate.now().year,
        val status : String = "",
        var vacationDays: Double = 15.0){
        fun getUserVacationHistory(){

        }
}