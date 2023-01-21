package pl.kul

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class Main

fun main(args: Array<String>) {
    val ctx = runApplication<Main>(*args)
    ctx.getBean(MongoTemplate::class.java).db.drop()
}

@Configuration
class Config {

    @Bean
    fun mongoConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(DataTimeAdapterToLocalDateTimeConverter(),
            DateToDateTimeAdapterConverter()))
    }

}