package br.challege.user.management.system.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(String token, String refreshToken) {
}