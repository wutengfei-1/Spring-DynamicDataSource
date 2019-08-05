package cn.sr.user.provider.ui.controller

import cn.sr.user.provider.api.dto.UserInfoDto
import cn.sr.user.provider.core.mapper.UserMapper
import cn.sr.user.provider.core.po.UserPo
import cn.sr.user.provider.core.service.UserService
import cn.sr.user.provider.ui.App
import com.ctrip.framework.apollo.ConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.interceptor.TransactionInterceptor
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class UserServiceController {

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/user/{id}")
    fun getUserInfoByUserId(@PathVariable("id") userId: Int): UserInfoDto {

        val userPo = userService.getUserById(userId)

        return UserInfoDto().apply {
            id = userPo.id
            desc = userPo.description
        }
    }

    @PostMapping("/user")
    fun addUserInfo(@RequestBody userInfoDto: UserInfoDto): String {
        //println(TransactionInterceptor.currentTransactionStatus())
        return userService.addUser(UserPo().apply {
            this.id = userInfoDto.id
            this.description = userInfoDto.desc
        }).toString()
    }


    @field:Value("\${dev.int}")
    var remoteConfigInt: Int? = -999999

    @GetMapping("/echo")
    fun echo(httpServletRequest: HttpServletRequest): String {
        for (name in httpServletRequest.headerNames) {
            for (value in httpServletRequest.getHeaders(name)) {
                //println(">>>>>>>>>> kv = $name + $value")
            }
        }
        return ConfigService.getAppConfig().getProperty("dev.string", "xxxx") + remoteConfigInt
    }

}