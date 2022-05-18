package com.example.kakaostyle.controller

import com.example.kakaostyle.model.User
import com.example.kakaostyle.service.LoginService
import com.fasterxml.jackson.databind.util.JSONPObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/login")
class LoginController(@Autowired private val loginService : LoginService) {
    @PostMapping()
    fun index(@RequestBody user : User): ResponseEntity<User> {
        try{
            var respUser = loginService.getLogin(user)
            return ResponseEntity.status(HttpStatus.OK).body(respUser)
        } catch (e : Exception){
            println(e)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Input is not in valid format")
        }

    }

}