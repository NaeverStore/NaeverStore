package com.naever.store

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing


@SpringBootApplication
@EnableJpaAuditing // JPA Auditing 기능을 활성화하기 위해 메인 애플리케이션에 @EnableJpaAuditing 어노테이션을 추가한다.
class NaeverStoreApplication
fun main(args: Array<String>) {
    runApplication<NaeverStoreApplication>(*args)
}
