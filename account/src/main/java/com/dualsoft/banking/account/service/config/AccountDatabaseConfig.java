package com.dualsoft.banking.account.service.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.dualsoft.banking.account.service.repository")
public class AccountDatabaseConfig {
}
