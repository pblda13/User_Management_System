package br.challege.user.management.system.service.impl;

import br.challege.user.management.system.domain.Usuario;
import br.challege.user.management.system.domain.exception.BusinessException;
import br.challege.user.management.system.dto.UsuarioDto;
import br.challege.user.management.system.producer.KafkaProducer;
import br.challege.user.management.system.repository.UsuarioRepository;
import br.challege.user.management.system.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public UsuarioDto salvar(UsuarioDto usuarioDto) {

        Usuario usuarioJaExiste = usuarioRepository.findByLogin(usuarioDto.login());

        if (usuarioJaExiste != null) {
            throw new BusinessException("Usuário já existe!");
        }

        var passwordHash = passwordEncoder.encode(usuarioDto.senha());

        Usuario entity = new Usuario(usuarioDto.nome(), usuarioDto.login(), passwordHash, usuarioDto.role());

        Usuario novoUsuario = usuarioRepository.save(entity);

        kafkaProducer.sendMessage("Usuario cadastrado com sucesso");

        return new UsuarioDto(novoUsuario.getNome(), novoUsuario.getLogin(), novoUsuario.getSenha(), novoUsuario.getRole());
    }
}