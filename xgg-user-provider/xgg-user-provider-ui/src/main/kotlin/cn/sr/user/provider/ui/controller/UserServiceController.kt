package cn.sr.user.provider.ui.controller

import cn.sr.user.provider.api.dto.UserInfoDto
import cn.sr.user.provider.ui.App
import com.ctrip.framework.apollo.ConfigService
import org.apache.catalina.core.ApplicationContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
class UserServiceController {

    @GetMapping("/user/{id}")
    fun getUserInfoByUserId(@PathVariable("id") userId: Int): UserInfoDto {
        var res = App.users.find { it.id == userId }
        if (res == null) {
            res = UserInfoDto()
        }
        return res
    }

    @PostMapping("/user")
    fun addUserInfo(@RequestBody userInfoDto: UserInfoDto) {
        if (userInfoDto.id != null && App.users.find { it.id == userInfoDto.id } == null) {
            App.users.add(userInfoDto)
        }
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