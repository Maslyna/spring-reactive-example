package com.example.reactive.router.mapper;

import com.example.reactive.entity.Account;
import com.example.reactive.router.response.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse accountToAccountResponse(Account account) {
        return AccountResponse.builder()
                .userId(account.getId())
                .username(account.getUsername())
                .role(account.getRole())
                .build();
    }
}
