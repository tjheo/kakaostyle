package com.example.kakaostyle.service

import com.example.kakaostyle.STATUS_ACTIVE
import com.example.kakaostyle.STATUS_CANCEL
import com.example.kakaostyle.STATUS_USE
import com.example.kakaostyle.controller.VacationController
import com.example.kakaostyle.controller.ValidUpdateBodyDTO
import com.example.kakaostyle.controller.ValidUpdateHistoryBodyDTO
import com.example.kakaostyle.dao.CustomLoginDao
import com.example.kakaostyle.dao.VacationDao
import com.example.kakaostyle.dao.VacationHistoryDao
import com.example.kakaostyle.model.User
import com.example.kakaostyle.model.UserVacation
import com.example.kakaostyle.model.UserVacationHistory
import org.springframework.data.domain.Sort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Objects
import javax.swing.text.html.parser.Entity

@Service
class VacationService(@Autowired private val vacationDao: VacationDao,@Autowired private val customLoginDao: CustomLoginDao, @Autowired private val vacationHistoryDao: VacationHistoryDao) {
    fun getMyVacationList(user : User) : List<UserVacation>? {
        this.checkVacationData(user)
        val sort = sortByYear()
        return vacationDao.findByUser(user, sort)
    }

    fun getMyVacation(@PathVariable id : Long, user:User) : Map<String, Any?> {
        this.checkVacationData(user)
        val vacation = vacationDao.findOneByUserAndId(user, id)
        val sort = sortByStartAt()
        return if (vacation != null){
            mapOf("vacation" to vacation, "history" to vacationHistoryDao.findByUserVacation(vacation, sort))

        }
        else mapOf("vacation" to null, "history" to null)
    }

    fun useMyVacation(@PathVariable id : Long, user:User, userVacationHistory: UserVacationHistory) : Map<String, Any?> {

        this.checkVacationData(user)
        val vacation = vacationDao.findOneByUserAndId(user, id)
        if (!vacationHistoryDao.hasDuplicateVacationHistory(userVacationHistory.startAt, userVacationHistory.endAt).isNullOrEmpty()){
            throw Exception("Duplicate Vacation Days")
        }
        if (vacation != null){
            if (vacation.vacationDays - userVacationHistory.vacationDays > 0){
                vacation.vacationDays -= userVacationHistory.vacationDays
                userVacationHistory.userVacation = vacation
                vacationDao.save(vacation)
                vacationHistoryDao.save(userVacationHistory)
                val sort = sortByStartAt()
                val history = vacationHistoryDao.findByUserVacation(vacation, sort)
                return mapOf("vacation" to vacation, "history" to history)
            }
            throw Exception("Bad Vacation Days")
        }
        else throw Exception("Invalid Vacation ID")
    }

    fun updateMyVacation(@PathVariable id : Long, user:User, validUpdateBodyDTO: ValidUpdateBodyDTO) : Map<String, Any?> {

        this.checkVacationData(user)
        val vacation = vacationDao.findOneByUserAndId(user, id)
        if (vacation != null){
            if (validUpdateBodyDTO.vacationDays == null || validUpdateBodyDTO.vacationDays < 0){
                throw Exception("Invalid Vacation Days")
            }
            vacation.vacationDays = validUpdateBodyDTO.vacationDays
            vacationDao.save(vacation)
            val sort = sortByStartAt()
            val history = vacationHistoryDao.findByUserVacation(vacation, sort)
            return mapOf("vacation" to vacation, "history" to history)
        }
        else throw Exception("Invalid Vacation ID")
    }

    fun updateMyVacationHistory(@PathVariable id : Long, user:User, validUpdateBodyDTO: ValidUpdateHistoryBodyDTO, @PathVariable hId : Long) : Map<String, Any?> {

        this.checkVacationData(user)
        val vacation = vacationDao.findOneByUserAndId(user, id) ?: throw Exception("Invalid Vacation ID")
        var vacationHistory = vacationHistoryDao.findOneById(hId) ?: throw Exception("Invalid Vacation History ID")
        if (validUpdateBodyDTO.status == STATUS_USE){
            if (validUpdateBodyDTO.startAt == null){
                throw Exception("StartAt Required")
            }
            if (validUpdateBodyDTO.endAt == null){
                throw Exception("endAt Required")
            }
            if (validUpdateBodyDTO.vacationDays == null){
                throw Exception("vacationDays Required")
            }
            vacationHistory.startAt = validUpdateBodyDTO.startAt
            vacationHistory.endAt = validUpdateBodyDTO.endAt
            vacationHistory.updateAt = LocalDateTime.now()
            val diff = validUpdateBodyDTO.vacationDays - vacationHistory.vacationDays
            vacationHistory.vacationDays = validUpdateBodyDTO.vacationDays
            if (validUpdateBodyDTO.reason != null){
                vacationHistory.reason = validUpdateBodyDTO.reason
            }
            vacation.vacationDays -= diff
            if (vacation.vacationDays < 0){
                throw Exception("Vacation Days Exceed")
            }
        }
        if (validUpdateBodyDTO.status == STATUS_CANCEL){
            vacationHistory.updateAt = LocalDateTime.now()
            vacationHistory.status = STATUS_CANCEL
            vacation.vacationDays += vacationHistory.vacationDays
        }
        vacationDao.save(vacation)
        vacationHistoryDao.save(vacationHistory)
        val sort = sortByStartAt()
        val history = vacationHistoryDao.findByUserVacation(vacation, sort)
        return mapOf("vacation" to vacation, "history" to history)
    }

    private fun checkVacationData(user: User) : Boolean {
        val vacation = vacationDao.findByUserAndYear(user, LocalDate.now().year)
        if (vacation == null){
            var userVacation = UserVacation(user=user, status=STATUS_ACTIVE)
            vacationDao.save(userVacation)
        }
        return true
    }

    private fun sortByYear(): Sort? {
        return Sort.by(Sort.Direction.DESC, "Year")
    }
    private fun sortByStartAt(): Sort? {
        return Sort.by(Sort.Direction.DESC, "startAt")
    }

}