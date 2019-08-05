package cn.sr.user.provider.core.config

import cn.sr.user.provider.core.datasource.DynamicDataSource
import com.ctrip.framework.apollo.ConfigService
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionFactoryBean
import org.mybatis.spring.annotation.MapperScan
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.Order
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource
import com.ctrip.framework.apollo.model.ConfigChange
import com.sun.xml.internal.ws.model.RuntimeModeler.getNamespace
import com.ctrip.framework.apollo.model.ConfigChangeEvent
import com.ctrip.framework.apollo.ConfigChangeListener
import com.ctrip.framework.apollo.enums.PropertyChangeType
import com.sun.java.accessibility.util.SwingEventMonitor.addChangeListener


@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = ["cn.sr.user.provider.core.mapper"])
open class MultipleDataSourceConfig {

    companion object {
        val master: String = "master"
        val threadLocal: ThreadLocal<String> = ThreadLocal()
    }

    @Value("\${db.master.main}")
    lateinit var masterDB: String

    @Bean("masterDataSource")
    @Primary
    open fun masterDataSource(): DataSource? {
        return getDataSource(masterDB, masterDB)?.value
    }

    /*@Bean("slaveDataSource")
    open fun slaveDataSource(): DataSource {
        return DataSourceBuilder.create().apply {
            this.username("root")
            this.password("root")
            this.url("jdbc:mysql://127.0.0.1:3307/db01")
            this.driverClassName("com.mysql.jdbc.Driver")
        }.build()
    }*/

    @Bean("dataSource")
    open fun dataSource(@Qualifier("masterDataSource") masterDataSource: DataSource,
                        @Qualifier("slaves") slaves: ConcurrentHashMap<Any, Any>): DynamicDataSource {

        slaves.put(master, masterDataSource)
        return DynamicDataSource().apply {
            this.setTargetDataSources(slaves)
            this.setDefaultTargetDataSource(masterDataSource)
        }
    }

    @Bean("sqlSessionFactory")
    open fun sqlSessionFactory(@Qualifier("dataSource") ds: DataSource): SqlSessionFactory {
        val bean = SqlSessionFactoryBean()
        bean.setDataSource(ds)

        return bean.getObject()
    }

    @Bean
    open fun transactionManager(@Qualifier("dataSource") ds: DataSource): DataSourceTransactionManager {
        return DataSourceTransactionManager(ds)
    }


    //db.slave.eclass
    //name=root,password=root,driver=com.mysql.jdbc.Driver,url=jdbc:mysql://127.0.0.1:3307/db01
    @Bean("slaves")
    open fun runtimeDataSource(): ConcurrentHashMap<Any, Any> {
        val map = ConcurrentHashMap<Any, Any>()
        val cfg = ConfigService.getAppConfig()
        val set = cfg.propertyNames
        for (key in set) {
            if (key.startsWith("db.slave.")) {
                val value = cfg.getProperty(key, "")
                val kv = getDataSource(key, value)
                if (kv != null) {
                    map.put(kv.key, kv.value)
                }
            }
        }

        println("<<<<<<------->>>>>>  " + map.toString())

        cfg.addChangeListener(ConfigChangeListener { changeEvent ->
            //println("Changes for namespace " + changeEvent.namespace)
            synchronized(this) {
                for (key in changeEvent.changedKeys()) {
                    print(" apollo change key = ${key} ")
                    val change = changeEvent.getChange(key)
                    println(" changeType = ${change.changeType} changePropertyName = ${change.propertyName}")
                    //println(String.format(change.propertyName, change.oldValue, change.newValue, change.changeType))
                    if (change.changeType.equals(PropertyChangeType.ADDED) && change.propertyName.startsWith("db.slave.")) {
                        val kv = getDataSource(change.propertyName, change.newValue)
                        println(" apollo add datasource name = ${kv?.key} value = ${kv?.value} ")
                        if (kv != null) {
                            map.put(kv.key, kv.value)
                        }
                    }
                }
            }
        })

        return map
    }

    fun getDataSource(key: String, value: String): Entry<String, DataSource>? {

        if (value == "") return null

        val map = mutableMapOf<String, String>()
        for (dbkv in value.split(",")) {
            val arr = dbkv.split("=")
            map.put(arr[0], arr[1])
        }

        return Entry<String, DataSource>(key.split(".")[2], DataSourceBuilder.create().apply {
            this.username(map["name"])
            this.password(map["password"])
            this.driverClassName(map["driver"])
            this.url(map["url"])
        }.build())

    }

    class Entry<K, V>(var key: K, var value: V)

}