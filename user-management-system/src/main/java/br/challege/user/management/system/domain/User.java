package br.challege.user.management.system.domain;

import br.challege.user.management.system.dto.UserDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false,unique = true)
    private String email;

    @Max(8)
    @Min(8)
    @NotBlank
    @Column(nullable = false)
    private String password;

    private UserRole role;

    public User(UserDTO userDTO){
        BeanUtils.copyProperties(userDTO,this);

    }

}
