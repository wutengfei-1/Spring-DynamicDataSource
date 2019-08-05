package cn.sr.user.provider.core.datasource

import cn.sr.user.provider.core.config.MultipleDataSourceConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.util.Assert
import java.util.concurrent.ConcurrentHashMap
import javax.sql.DataSource

class DynamicDataSource : AbstractRoutingDataSource() {

    @Autowired
    @Qualifier("slaves")
    lateinit var dbs: ConcurrentHashMap<Any, Any>

    override fun determineCurrentLookupKey(): Any {
        val key = MultipleDataSourceConfig.threadLocal.get()

        println(" DynamicDataSource 当前使用的数据库为 : ${key} ")


        if (key == null) {
            println("error dataSource key ")
            return MultipleDataSourceConfig.master
        }

        return key
    }

    override fun determineTargetDataSource(): DataSource? {
        val lookupKey = determineCurrentLookupKey()
        var dataSource = dbs[lookupKey]

        if (dataSource is DataSource) {
            return dataSource
        }

        return null
    }

}