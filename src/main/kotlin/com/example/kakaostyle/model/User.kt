package com.example.kakaostyle.model
import javax.persistence.*

@Entity
@Table(name = "user")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id : Int = 0,
        val email : String = "",
        val firstname : String = "",
        val lastname : String = "",
        var password : String = "",
        val accessToken : String = ""){



}