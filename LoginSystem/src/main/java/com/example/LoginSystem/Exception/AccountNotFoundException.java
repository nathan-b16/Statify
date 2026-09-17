package com.example.LoginSystem.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountNotFoundException extends RuntimeException {

    private final String message;
}
