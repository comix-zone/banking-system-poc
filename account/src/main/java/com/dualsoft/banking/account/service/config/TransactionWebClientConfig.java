package com.dualsoft.banking.account.service.config;
import com.dualsoft.banking.account.service.clients.TransactionClient;
import com.dualsoft.banking.account.service.exception.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Configuration
public class TransactionWebClientConfig {


    @Value("${clients.transaction.url}")
    private String transactionClientUrl;
    @Bean("transactionclient")
    public WebClient transactionWebClient() {
        final int size = 16 * 1024 * 1024;
        return WebClient.builder()
                .baseUrl(this.transactionClientUrl)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                        .build())
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, this::handle4xxClientError)
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, this::handle5xxServerError)
                .build();
    }

    private Mono<? extends Throwable> handle5xxServerError(ClientResponse clientResponse) {
        return Mono.error(AccountGeneralRuntimeException::new);
    }

    private Mono<? extends Throwable> handle4xxClientError(ClientResponse clientResponse) {
        if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(AccountNotFoundRuntimeException::new);
        }

        if (clientResponse.statusCode() == HttpStatus.CONFLICT) {
            return Mono.error(AccountAlreadyExistsRuntimeException::new);
        }

         return Mono.error(AccountBalanceInsufficientRuntimeException::new);
    }

    @Bean("RestTransactionClient")
    public TransactionClient restGenericOperatorClient(@Qualifier("transactionclient") WebClient webClient) {
        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(WebClientAdapter.create(webClient))
                .build()
                .createClient(TransactionClient.class);
    }
}
