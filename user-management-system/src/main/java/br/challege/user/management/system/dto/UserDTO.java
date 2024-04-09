package br.challege.user.management.system.dto;

import br.challege.user.management.system.domain.User;
import br.challege.user.management.system.domain.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;

    public UserDTO(User user){
        BeanUtils.copyProperties(user,this);

    }
}
