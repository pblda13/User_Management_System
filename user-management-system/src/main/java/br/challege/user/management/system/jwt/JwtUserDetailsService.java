package br.challege.user.management.system.jwt;

import br.challege.user.management.system.domain.UserEntity;

import br.challege.user.management.system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findByUsername(username);
        return new JwtUserDetails(user);
    }

    public JwtToken getTokenAuthenticated (String username){
        UserEntity.Role role = userService.FindByRolePerUsername(username);
        return JwtUtils.createToken(username,role.name().substring("ROLE_".length()));
    }
}
