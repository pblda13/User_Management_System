package br.challege.user.management.system.service;

import br.challege.user.management.system.dto.AuthDto;
import br.challege.user.management.system.dto.TokenResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AutenticacaoService extends UserDetailsService {
    public TokenResponseDto obterToken(AuthDto authDto);
    public String validaTokenJwt(String token);

    TokenResponseDto obterRefreshToken(String s);
}