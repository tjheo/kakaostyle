package com.example.kakaostyle.model
import com.example.kakaostyle.STATUS_ACTIVE
import com.example.kakaostyle.dao.VacationDao
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.NoArgsConstructor
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Lazy
import javax.persistence.*

@Entity
@NoArgsConstructor
@EntityListeners(value = [UserListener::class])
@Table(name = "user")
class User(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id : Int = 0,
        val email : String = "",
        val firstname : String = "",
        val lastname : String = "",
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        var password : String = "",
        val accessToken : String = "",
){
}

@NoArgsConstructor
@Component
class UserListener(@Lazy private val vacationDao: VacationDao) {
    @PostPersist fun onPostPersist(user: User) {
        val userVacation = UserVacation(user=user, status=STATUS_ACTIVE)
        vacationDao.save(userVacation)
    }
}