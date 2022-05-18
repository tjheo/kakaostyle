package com.example.kakaostyle.controller

import com.example.kakaostyle.STATUS_USE
import com.example.kakaostyle.model.UserVacation
import com.example.kakaostyle.model.UserVacationHistory
import com.example.kakaostyle.service.LoginService
import com.example.kakaostyle.service.VacationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

@RestController
@RequestMapping("/vacations")
class VacationController(@Autowired private val vacationService: VacationService, @Autowired private val loginService: LoginService) {
    @GetMapping()
    fun index(@RequestHeader(value="access_token", required = false) accessToken : String?): ResponseEntity<List<UserVacation>> {
        try{
            val user = loginService.TokenExchange(accessToken) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access Token Required")
            return ResponseEntity.status(HttpStatus.OK).body(vacationService.getMyVacationList(user))
        } catch (e : Exception){
            println(e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid Access Token")
        }
    }
    @GetMapping("/{id}")
    fun VacationDetail(@RequestHeader(value="access_token", required = false) accessToken : String?,
                       @PathVariable id : Long): ResponseEntity<Map<String, Any?>> {
        try{
            val user = loginService.TokenExchange(accessToken) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access Token Required")
            return ResponseEntity.status(HttpStatus.OK).body(vacationService.getMyVacation(id, user))
        } catch (e : Exception){
            if (e.message == "Access Token Required"){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Access Token")
            }
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid Access Token")
        }
    }
    @PostMapping("/{id}")
    fun useVacation(@RequestHeader(value="access_token", required = false) accessToken : String?,
                    @PathVariable id : Long, @RequestBody userVacationHistory: UserVacationHistory): ResponseEntity<Map<String, Any?>> {
        try{
            val user = loginService.TokenExchange(accessToken) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access Token Required")
            userVacationHistory.status = STATUS_USE
            if (userVacationHistory.vacationDays % 0.25 == 0.00 && userVacationHistory.vacationDays != 0.00) {
                return ResponseEntity.status(HttpStatus.OK).body(vacationService.useMyVacation(id, user, userVacationHistory))
            }
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Bad Vacation Days")
        } catch (e : Exception){
            println(e.message)
            if (e.message == "Duplicate Vacation Days"){
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Duplicate Vacation Days")
            }
            if (e.message == "Access Token Required"){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Access Token")
            }
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid Access Token")
        }
    }
    @PutMapping("/{id}")
    fun updateVacationDays(@RequestHeader(value="access_token", required = false) accessToken : String?,
                       @PathVariable id : Long, @RequestBody validUpdateBodyDTO: ValidUpdateBodyDTO): ResponseEntity<Map<String, Any?>> {
        try{
            val user = loginService.TokenExchange(accessToken) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access Token Required")
            if (validUpdateBodyDTO.vacationDays == null){
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Bad Vacation Days")
            }
            return ResponseEntity.status(HttpStatus.OK).body(vacationService.updateMyVacation(id, user, validUpdateBodyDTO))
        } catch (e : Exception){
            println(e.message)
            if (e.message == "Invalid Vacation ID") {
                throw ResponseStatusException(HttpStatus.NOT_FOUND, "Vacation Not Found")
            }
            if (e.message == "Access Token Required"){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Access Token")
            }
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid Access Token")
        }
    }

    @PutMapping("/{id}/histories/{hId}")
    fun updateVacationHistory(@RequestHeader(value="access_token", required = false) accessToken : String?,
                              @PathVariable id : Long, @RequestBody validUpdateHistoryBodyDTO: ValidUpdateHistoryBodyDTO, @PathVariable hId: Long): ResponseEntity<Map<String, Any?>> {
        try{
            val user = loginService.TokenExchange(accessToken) ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Access Token Required")
            if (validUpdateHistoryBodyDTO.status == null){
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Status Required")
            }
            return ResponseEntity.status(HttpStatus.OK).body(vacationService.updateMyVacationHistory(id, user, validUpdateHistoryBodyDTO, hId))
        } catch (e : Exception){
            println(e.message)
            if (e.message == "Access Token Required"){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Access Token")
            }
            if (e.message == "Invalid Vacation ID"){
                throw ResponseStatusException(HttpStatus.NOT_FOUND,"Invalid Vacation ID")
            }
            if (e.message == "Invalid Vacation History ID"){
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid Vacation History ID")
            }
            if (e.message == "Vacation Days Exceed"){
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Vacation Days Exceed")
            }
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Invalid Access Token")
        }
    }
}
class ValidUpdateBodyDTO {

    @NotBlank
    val vacationDays: Double? = null

}
class ValidUpdateHistoryBodyDTO (
    @NotBlank
    val status: String? = null,
    val vacationDays: Double? = null,
    val startAt: LocalDateTime? = null,
    val endAt: LocalDateTime? = null,
    val reason: String? = null
)
//
//@RestController
//@RequestMapping("/login")
//class LoginController(@Autowired private val loginService : LoginService) {
//    @PostMapping()
//    fun index(@RequestBody user : User): ResponseEntity<User> {
//        try{
//            var respUser = loginService.getLogin(user)
//            respUser.password = "*******"
//            return ResponseEntity.status(HttpStatus.OK).body(respUser)
//        } catch (e : Exception){
//            println(e)
//            throw ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Input is not in valid format")
//        }
//
//    }
//
//}