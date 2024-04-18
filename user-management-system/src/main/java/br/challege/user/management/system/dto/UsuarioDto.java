package br.challege.user.management.system.dto;

import br.challege.user.management.system.enums.RoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public record UsuarioDto(
        String nome,
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\n")
        String login,
        @Min(8)
        @Max(8)
        String senha,
        RoleEnum role
) {
}