package com.matefoldes.exchangerates.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
open class RestTemplateConfig {

    @Bean
    open fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate =
        restTemplateBuilder.build()
}
