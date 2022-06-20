package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.UserDto
import com.invictoprojects.marketplace.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.persistence.EntityNotFoundException


@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PutMapping("{id}")
    fun updateUserInfo(@PathVariable id: Long, @RequestBody userDto: UserDto): ResponseEntity<Any> {
        return try {
            val user = MappingUtils.convertToEntity(userDto)
            user.id = id
            val result = userService.update(user)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: EntityNotFoundException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

}
