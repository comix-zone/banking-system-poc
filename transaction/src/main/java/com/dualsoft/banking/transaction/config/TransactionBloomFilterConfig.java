package com.dualsoft.banking.transaction.config;
import io.rebloom.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionBloomFilterConfig {

    @Value("$spring.redis.host")
    private String REDIS_HOST;
//dodati verovatnocu i sve ostale parametre
    @Value("${spring.redis.port}")
    private int REDIS_PORT = 6379;

    @Bean
    public Client bloomFilterClient() {
        return new Client(REDIS_HOST, REDIS_PORT);
    }
}
