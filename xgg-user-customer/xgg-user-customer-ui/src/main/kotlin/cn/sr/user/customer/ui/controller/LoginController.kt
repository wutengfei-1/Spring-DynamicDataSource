package cn.sr.user.customer.ui.controller

import cn.sr.user.provider.api.UserServiceApi
import cn.sr.user.provider.api.dto.UserInfoDto
import com.alibaba.dubbo.config.annotation.Reference
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

    @Reference
    lateinit var userServiceApi: UserServiceApi

    @PostMapping("/user/login")
    fun login(@RequestBody userInfoDto: UserInfoDto): Map<String, String> {
        val id = userInfoDto.id;
        val map = mutableMapOf<String, String>()
        var info: UserInfoDto?=null
        if (id != null) {
            info = userServiceApi.getUserInfoByUserId(id);
        }

        if (info != null && info.id != null) {
            map["id"] = info.id.toString()
            map["desc"] = info.desc
        } else {
            map["key"] = "user not exist"
        }

        return map
    }

    @GetMapping("/echo")
    fun echo(): String {
        return "echo"
    }

}