package cn.sr.user.provider.core.service

import cn.sr.user.provider.core.mapper.UserMapper
import cn.sr.user.provider.core.po.UserPo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.lang.RuntimeException
import java.util.*

@Service
open class UserService {

    @Autowired
    lateinit var userMapper: UserMapper

    @Transactional(readOnly = true)
    open fun getUserById(id: Int): UserPo {

        try {
            println(" UserService 的 getUser()方法 : 事务已经开启 -> " + TransactionInterceptor.currentTransactionStatus().toString())
        } catch (e: Exception) {
            println(" UserService 的 getUser()方法 : 事务没有开启 ")
        }

        val userPo = userMapper.selectUserById(id)
        if (userPo == null) {
            return UserPo().apply { description = "user not exist" }
        }

        return userPo
    }

    @Transactional
    open fun addUser(userPo: UserPo): Int {
        val id = userPo.id

        var res = 0

        try {
            println(" UserService 的 addUser()方法 : 事务已经开启 -> " + TransactionInterceptor.currentTransactionStatus().toString())
        } catch (e: Exception) {
            println(" UserService 的 addUser()方法 : 事务没有开启 ")
        }


        if (id != null && getUserById(id).id == null) {
            res = userMapper.insertUser(userPo)
        }

        /*val r = Random()
        if (r != null && r.nextBoolean()) {
            throw RuntimeException()
        } else {

        }*/


        return res
    }

}