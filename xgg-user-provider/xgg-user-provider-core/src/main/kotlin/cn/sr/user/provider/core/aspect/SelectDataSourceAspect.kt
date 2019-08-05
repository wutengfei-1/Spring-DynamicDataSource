package cn.sr.user.provider.core.aspect

import cn.sr.user.provider.core.config.MultipleDataSourceConfig
import cn.sr.user.provider.core.po.UserPo
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.annotation.Order
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.stereotype.Component
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource


@Component
@Aspect
@Order(1)
class SelectDataSourceAspect {

    val random = Random(76478347)

    @Autowired
    @Qualifier("slaves")
    lateinit var dbs: ConcurrentHashMap<Any, Any>

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    fun before(jp: JoinPoint) {
        println(" SelectDataSourceAspect 拦截的方法名为 ${jp.signature.name} ")

        try {
            println(" SelectDataSourceAspect 执行${jp.signature.name}方法 : 事务已经开启 -> " + TransactionInterceptor.currentTransactionStatus().toString())
            //println(TransactionInterceptor.currentTransactionStatus().toString() + " " + txm)
        } catch (e: Exception) {
            println(" SelectDataSourceAspect 执行${jp.signature.name}方法 : 事务没有开启 ")
        }


        /*if (jp.signature.name.equals("addUser")) {
            val arg = jp.args[0]
            if (arg is UserPo) {
                val id = arg.id
                if (id != null && id % 2 == 1) {
                    MultipleDataSourceConfig.threadLocal.set(MultipleDataSourceConfig.master)
                    println(">>>>>>>>>>   master")
                } else {
                    MultipleDataSourceConfig.threadLocal.set(MultipleDataSourceConfig.slave)
                    println(">>>>>>>>>>   slave")
                }
            }

        }*/

        val methodName = jp.signature.name
        println(" dbs.keys = ${dbs.keys} ")
        if (methodName.startsWith("add") || dbs.size == 1) {
            MultipleDataSourceConfig.threadLocal.set(MultipleDataSourceConfig.master)
        } else {
            val li = mutableListOf<String>()
            for (k in dbs.keys) {
                if (!MultipleDataSourceConfig.master.equals(k.toString())) {
                    li.add(k.toString())
                }
            }

            val idx = Math.abs(random.nextInt()) % li.size
            MultipleDataSourceConfig.threadLocal.set(li[idx])
        }


    }

}