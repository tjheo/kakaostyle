package com.example.kakaostyle.model
import javax.persistence.*

@Entity
@Table(name = "user_vacation")
class UserVacation(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id : Long = 0,
        @ManyToOne val user : User,
        val year : String = "",
        val vacation: Double = 15.0){
}