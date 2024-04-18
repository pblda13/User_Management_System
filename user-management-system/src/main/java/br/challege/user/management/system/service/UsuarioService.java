package br.challege.user.management.system.service;

import br.challege.user.management.system.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto salvar(UsuarioDto usuarioDto);

    UsuarioDto atualizar(Long id, UsuarioDto usuarioDto);

    UsuarioDto findById(Long id);

    UsuarioDto findByLogin(String login);

    void deletar(Long id);

    List<UsuarioDto> findAll();
}