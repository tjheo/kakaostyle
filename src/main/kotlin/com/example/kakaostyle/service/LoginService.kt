package com.example.kakaostyle.service

import com.example.kakaostyle.dao.CustomLoginDao
import com.example.kakaostyle.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

@Service
class LoginService(@Autowired private val customLoginDao: CustomLoginDao) {
    fun getLogin(@RequestBody user : User) : User {
        if(!user.email.isNullOrEmpty() && !user.password.isNullOrEmpty()){
            if(customLoginDao.findOneByEmailAndPassword(user.email, user.password) !== null)
                return customLoginDao.findOneByEmailAndPassword(user.email, user.password)!!
            else throw Exception("Email or Password is not valid")
        }
        else throw Exception("Name cannot contains non alphabet characters")
    }
    fun TokenExchange(accessToken: String?): User? {
        accessToken ?: return null
        return customLoginDao.findByAccessToken(accessToken) ?: throw Exception("Unauthorized")

    }

}