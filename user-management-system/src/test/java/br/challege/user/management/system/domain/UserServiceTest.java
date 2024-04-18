package br.challege.user.management.system.domain;

import br.challege.user.management.system.domain.exception.UserNotFoundException;
import br.challege.user.management.system.domain.exception.UsernameUniqueViolationException;
import br.challege.user.management.system.dto.UsuarioDto;
import br.challege.user.management.system.enums.RoleEnum;
import br.challege.user.management.system.producer.KafkaProducer;
import br.challege.user.management.system.repository.UsuarioRepository;
import br.challege.user.management.system.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

// Importação da extensão Mockito para uso de mocks
@ExtendWith(MockitoExtension.class)
// Classe de teste para o UserService
class UserServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    public void testSalvar() {
        // Criando um usuário DTO para teste
        UsuarioDto usuarioDto = new UsuarioDto("Nome", "login", "senha", RoleEnum.USER);

        // Configurando o comportamento do repository mock
        when(usuarioRepository.findByLogin("login")).thenReturn(null); // O usuário não existe ainda
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            // Simulando o salvamento do usuário e retornando um usuário válido
            Usuario usuarioSalvo = invocation.getArgument(0);
            usuarioSalvo.setId(1L); // Suponha que o ID seja atribuído ao salvar no banco de dados
            return usuarioSalvo;
        });
        when(passwordEncoder.encode("senha")).thenReturn("senhaCriptografada");

        // Chamando o método salvar
        UsuarioDto result = usuarioService.salvar(usuarioDto);

        // Verificando se o usuário foi salvo corretamente
        assertEquals("Nome", result.nome());
        assertEquals("login", result.login());
        assertEquals("senhaCriptografada", result.senha());
        assertEquals(RoleEnum.USER, result.role());

        // Verificando se o método sendMessage foi chamado no kafkaProducer mock
        verify(kafkaProducer).sendMessage("Usuario cadastrado com sucesso");
    }

    @Test
    public void testAtualizar() {
        // Criando um usuário DTO para teste
        UsuarioDto usuarioDto = new UsuarioDto("Novo Nome", "novoLogin", "novaSenha", RoleEnum.ADMIN);

        // Configurando o comportamento do repository mock
        Usuario usuarioExistente = new Usuario("Nome Antigo", "loginAntigo", "senhaAntiga", RoleEnum.USER);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(passwordEncoder.encode("novaSenha")).thenReturn("novaSenhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {

            // Simulando a atualização do usuário e retornando o usuário atualizado
            Usuario usuarioAtualizado = invocation.getArgument(0);
            usuarioAtualizado.setNome(usuarioDto.nome());
            usuarioAtualizado.setLogin(usuarioDto.login()); // Atualizando o login
            usuarioAtualizado.setSenha(usuarioDto.senha());
            usuarioAtualizado.setRole(usuarioDto.role());
            return usuarioAtualizado;
        });

        // Chamando o método atualizar
        UsuarioDto result = usuarioService.atualizar(1L, usuarioDto);

        // Verificando se o usuário foi atualizado corretamente
        assertEquals("Novo Nome", result.nome());
        assertEquals("novoLogin", result.login()); // Agora esperamos o novo login
        assertEquals("novaSenha", result.senha());
        assertEquals(RoleEnum.ADMIN, result.role());

        // Verificando se o método sendMessage foi chamado no kafkaProducer mock
        verify(kafkaProducer).sendMessage("Usuário atualizado com sucesso");
    }


    @Test
    public void testFindById() {
        // Configurando o comportamento do repository mock
        Usuario usuario = new Usuario("Nome", "login", "senha", RoleEnum.USER);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Chamando o método findById
        UsuarioDto result = usuarioService.findById(1L);

        // Verificando se o usuário retornado é o esperado
        assertEquals("Nome", result.nome());
        assertEquals("login", result.login());
        assertEquals("senha", result.senha());
        assertEquals(RoleEnum.USER, result.role());
    }

    @Test
    public void testFindByLogin() {
        // Configurando o comportamento do repository mock
        Usuario usuario = new Usuario("Nome", "login", "senha",  RoleEnum.USER);
        when(usuarioRepository.findByLogin("login")).thenReturn(usuario);

        // Chamando o método findByLogin
        UsuarioDto result = usuarioService.findByLogin("login");

        // Verificando se o usuário retornado é o esperado
        assertEquals("Nome", result.nome());
        assertEquals("login", result.login());
        assertEquals("senha", result.senha());
        assertEquals(RoleEnum.USER, result.role());
    }

    @Test
    public void testDeletar() {
        // Configurando o comportamento do repository mock
        Usuario usuario = new Usuario("Nome", "login", "senha",  RoleEnum.USER);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Chamando o método deletar
        usuarioService.deletar(1L);

        // Verificando se o método deleteById foi chamado no repository mock
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    public void testFindAll() {
        // Configurando o comportamento do repository mock
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("Nome1", "login1", "senha1", RoleEnum.USER));
        usuarios.add(new Usuario("Nome2", "login2", "senha2", RoleEnum.ADMIN));
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Chamando o método findAll
        List<UsuarioDto> result = usuarioService.findAll();

        // Verificando se a lista de usuários retornada contém os usuários esperados
        assertEquals(2, result.size());
        assertEquals("Nome1", result.get(0).nome());
        assertEquals("login1", result.get(0).login());
        assertEquals("senha1", result.get(0).senha());
        assertEquals( RoleEnum.USER, result.get(0).role());
        assertEquals("Nome2", result.get(1).nome());
        assertEquals("login2", result.get(1).login());
        assertEquals("senha2", result.get(1).senha());
        assertEquals(RoleEnum.ADMIN, result.get(1).role());
    }
}


