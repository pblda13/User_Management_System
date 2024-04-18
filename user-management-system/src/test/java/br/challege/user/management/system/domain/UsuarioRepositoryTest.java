package br.challege.user.management.system.domain;

import br.challege.user.management.system.enums.RoleEnum;
import br.challege.user.management.system.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testSaveAndFindById() {
        // Criando um novo usuário
        Usuario usuario = new Usuario("Nome", "login", "senha", RoleEnum.USER);
        // Salvando o usuário no banco de dados
        Usuario savedUsuario = usuarioRepository.save(usuario);

        // Buscando o usuário pelo ID
        Usuario foundUsuario = usuarioRepository.findById(savedUsuario.getId()).orElse(null);

        // Verificando se o usuário encontrado é o mesmo que foi salvo
        assertNotNull(foundUsuario);
        assertEquals("Nome", foundUsuario.getNome());
        assertEquals("login", foundUsuario.getLogin());
        assertEquals("senha", foundUsuario.getSenha());
        assertEquals(RoleEnum.USER, foundUsuario.getRole());
    }

    @Test
    public void testFindByLogin() {
        // Criando um novo usuário
        Usuario usuario = new Usuario("Nome", "login", "senha", RoleEnum.USER);

        // Salvando o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Buscando o usuário pelo login
        Usuario foundUsuario = usuarioRepository.findByLogin("login");

        // Verificando se o usuário encontrado é o mesmo que foi salvo
        assertNotNull(foundUsuario);
        assertEquals("Nome", foundUsuario.getNome());
        assertEquals("login", foundUsuario.getLogin());
        assertEquals("senha", foundUsuario.getSenha());
        assertEquals(RoleEnum.USER, foundUsuario.getRole());
    }

    @Test
    public void testDelete() {
        // Criando um novo usuário
        Usuario usuario = new Usuario("Nome", "login", "senha", RoleEnum.USER);

        // Salvando o usuário no banco de dados
        usuarioRepository.save(usuario);

        // Deletando o usuário do banco de dados
        usuarioRepository.delete(usuario);

        // Tentando encontrar o usuário após a exclusão
        Usuario foundUsuario = usuarioRepository.findByLogin("login");

        // Verificando se o usuário não foi encontrado após a exclusão
        assertNull(foundUsuario);
    }

    @Test
    public void testFindAll() {
        // Criando alguns usuários de exemplo
        Usuario usuario1 = new Usuario("Nome", "login", "senha", RoleEnum.USER);
        Usuario usuario2 = new Usuario("Nome", "login", "senha", RoleEnum.ADMIN);

        // Salvando os usuários no banco de dados
        usuarioRepository.save(usuario1);
        usuarioRepository.save(usuario2);

        // Buscando todos os usuários do banco de dados
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Verificando se a lista de usuários não está vazia e contém os usuários salvos
        assertNotNull(usuarios);
        assertEquals(2, usuarios.size());
    }
}

