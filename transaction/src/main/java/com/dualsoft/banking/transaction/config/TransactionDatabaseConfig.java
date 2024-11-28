package com.dualsoft.banking.transaction.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.dualsoft.banking.transaction.service.repository")
public class TransactionDatabaseConfig {
}
