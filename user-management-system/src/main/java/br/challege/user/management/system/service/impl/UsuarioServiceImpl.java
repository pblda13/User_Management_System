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

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

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

    @Override
    public UsuarioDto atualizar(Long id, UsuarioDto usuarioDto) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            throw new BusinessException("Usuário não encontrado!");
        }

        Usuario usuario = usuarioOptional.get();
        usuario.setNome(usuarioDto.nome());
        usuario.setSenha(passwordEncoder.encode(usuarioDto.senha()));
        usuario.setRole(usuarioDto.role());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);

        kafkaProducer.sendMessage("Usuário atualizado com sucesso");

        return new UsuarioDto(usuarioAtualizado.getNome(), usuarioAtualizado.getLogin(), usuarioAtualizado.getSenha(), usuarioAtualizado.getRole());
    }

    @Override
    public UsuarioDto findById(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            throw new BusinessException("Usuário não encontrado!");
        }

        Usuario usuario = usuarioOptional.get();

        return new UsuarioDto(usuario.getNome(), usuario.getLogin(), usuario.getSenha(), usuario.getRole());
    }

    @Override
    public UsuarioDto findByLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);

        if (usuario == null) {
            throw new BusinessException("Usuário não encontrado!");
        }

        return new UsuarioDto(usuario.getNome(), usuario.getLogin(), usuario.getSenha(), usuario.getRole());
    }

    @Override
    public void deletar(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);

        if (usuarioOptional.isEmpty()) {
            throw new BusinessException("Usuário não encontrado!");
        }

        usuarioRepository.deleteById(id);

    }

    @Override
    public List<UsuarioDto> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        List<UsuarioDto> usuariosDto = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            usuariosDto.add(new UsuarioDto(usuario.getNome(), usuario.getLogin(), usuario.getSenha(), usuario.getRole()));
        }

        return usuariosDto;
    }
}