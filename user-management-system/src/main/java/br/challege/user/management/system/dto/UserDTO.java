package br.challege.user.management.system.dto;

import br.challege.user.management.system.domain.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {


    private Long id;

    @NotBlank
    @Email(message = "formato do e-mail está invalido")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;

    private UserEntity.Role role;

}
