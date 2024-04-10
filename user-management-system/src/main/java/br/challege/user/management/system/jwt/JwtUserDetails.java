package br.challege.user.management.system.jwt;


import br.challege.user.management.system.domain.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class JwtUserDetails extends User {

    private UserEntity userEntity;
    public JwtUserDetails(UserEntity userEntity) {
        super(userEntity.getUsername(),userEntity.getPassword(), AuthorityUtils.createAuthorityList(userEntity.getRole().name()));
        this.userEntity = userEntity;
    }
    public Long getId(){
        return this.userEntity.getId();
    }
    public String getRole(){
        return this.userEntity.getRole().name();
    }
}
