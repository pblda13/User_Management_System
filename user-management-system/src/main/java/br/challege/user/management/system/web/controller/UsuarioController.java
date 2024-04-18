package br.challege.user.management.system.web.controller;

import br.challege.user.management.system.dto.UsuarioDto;
import br.challege.user.management.system.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public UsuarioDto salvar(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.salvar(usuarioDto);
    }

    @PutMapping("/{id}")
    public UsuarioDto atualizar(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        return usuarioService.atualizar(id, usuarioDto);
    }

    @GetMapping("/{id}")
    public UsuarioDto getById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/buscar/{username}")
    public UsuarioDto getByLogin(@PathVariable String login) {
        return usuarioService.findByLogin(login);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }

    @GetMapping
    public List<UsuarioDto> getAll() {
        return usuarioService.findAll();
    }
}
