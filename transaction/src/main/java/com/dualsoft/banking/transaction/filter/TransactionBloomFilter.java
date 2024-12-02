package com.dualsoft.banking.transaction.filter;

import com.dualsoft.banking.transaction.dto.TransferMoneyRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rebloom.client.Client;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
public class TransactionBloomFilter extends OncePerRequestFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final Client bloomFilterClient;
    private final String filterName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var data = mapRequestBody(request, TransferMoneyRequestDTO.class);
        String item = data.getCorrelationId().toString();


        if (bloomFilterClient.exists(filterName, item)) {
            log.info("Item '{}' already exists in the Bloom filter '{}'.", item, filterName);
         //odradi logiku za uzimanje, potrazi u servisu, ako postoji - vrati ako ne postoji odradi
            return;
        }
        filterChain.doFilter(request, response);
    }



    public static <T> T mapRequestBody(HttpServletRequest request, Class<T> clazz) throws IOException {
        // Read the request body as a String
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        // Convert the JSON String to the desired object
        return objectMapper.readValue(stringBuilder.toString(), clazz);
    }
}
