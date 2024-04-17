package br.challege.user.management.system.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class ApiError {
    private final String message;

    public ApiError(String message) {
        this.message = message;
    }
}