package br.challege.user.management.system.dto;

import br.challege.user.management.system.enums.RoleEnum;
import lombok.Setter;



public record UsuarioDto(
        String nome,
        String login,
        String senha,
        RoleEnum role
) {
}