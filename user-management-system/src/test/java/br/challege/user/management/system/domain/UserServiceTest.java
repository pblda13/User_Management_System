package br.challege.user.management.system.domain;

import br.challege.user.management.system.domain.exception.UserNotFoundException;
import br.challege.user.management.system.domain.exception.UsernameUniqueViolationException;
import br.challege.user.management.system.producer.KafkaProducer;
import br.challege.user.management.system.repository.UserRepository;
import br.challege.user.management.system.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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

    // Mock do UserRepository
    @Mock
    private UserRepository userRepository;

    // Mock do PasswordEncoder
    @Mock
    private PasswordEncoder passwordEncoder;

    // Mock do KafkaProducer
    @Mock
    private KafkaProducer kafkaProducer;

    // Instância do UserService a ser testado, com mocks injetados
    @InjectMocks
    private UserService userService;

    // Teste de criação de usuário bem-sucedido
    @Test
    void testCreateUser_Success() {
        // Arrange
        // Cria um usuário de teste
        UserEntity user = new UserEntity(null, "testuser", "password", UserEntity.Role.ROLE_ADMIN);
        // Configura o comportamento dos mocks
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        // Act
        // Chama o método a ser testado
        UserEntity createdUser = userService.create(user);

        // Assert
        // Verifica os resultados
        assertNotNull(createdUser); // Garante que o usuário criado não seja nulo
        assertEquals("encodedPassword", createdUser.getPassword()); // Verifica se a senha foi corretamente codificada
        assertEquals("testuser", createdUser.getUsername()); // Verifica se o nome de usuário é o esperado
        assertEquals(UserEntity.Role.ROLE_ADMIN, createdUser.getRole()); // Verifica se a função do usuário é a esperada
        verify(kafkaProducer, times(1)).sendMessage(anyString()); // Verifica se o método sendMessage do KafkaProducer foi chamado
    }

    // Teste de falha na criação de usuário (quando o nome de usuário já existe)
    @Test
    void testCreateUser_Failure_UsernameExists() {
        // Arrange
        // Cria um usuário de teste com nome existente
        UserEntity user = new UserEntity(null, "existingUser", "password", UserEntity.Role.ROLE_ADMIN);
        // Simula uma exceção ao tentar salvar no repositório
        when(userRepository.save(any(UserEntity.class))).thenThrow(DataIntegrityViolationException.class);

        // Assert
        // Verifica se uma exceção é lançada ao tentar criar o usuário
        assertThrows(UsernameUniqueViolationException.class, () -> {
            // Act
            userService.create(user);
        });
    }

    // Teste de listagem de todos os usuários
    @Test
    void testListAllUsers() {
        // Arrange
        // Cria uma lista de usuários de teste
        List<UserEntity> userList = new ArrayList<>();
        userList.add(new UserEntity(1L, "user1", "password1", UserEntity.Role.ROLE_CLIENTE));
        userList.add(new UserEntity(2L, "user2", "password2", UserEntity.Role.ROLE_ADMIN));
        // Configura o comportamento do mock para retornar essa lista
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        // Chama o método a ser testado
        List<UserEntity> result = userService.listAll();

        // Assert
        // Verifica os resultados
        assertEquals(2, result.size()); // Verifica se o número de usuários retornados é o esperado
        assertEquals(userList.get(0), result.get(0)); // Verifica se o primeiro usuário é o esperado
        assertEquals(userList.get(1), result.get(1)); // Verifica se o segundo usuário é o esperado
    }

    // Teste de atualização de usuário bem-sucedida
    @Test
    public void testUpdateUser_Success() {
        // Arrange
        // Identificador do usuário a ser atualizado
        Long userId = 1L;
        // Dados do usuário existente
        String existingUsername = "user1";
        String existingPassword = "password1";
        UserEntity existingUser = new UserEntity(userId, existingUsername, existingPassword, UserEntity.Role.ROLE_CLIENTE);

        // Configura o comportamento dos mocks
        when(userRepository.existsById(userId)).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedNewPassword");

        // Simula a lógica de salvamento com codificação de senha
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity user = invocation.getArgument(0);
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Codifica a senha aqui
            return user;
        });

        // Act
        // Chama o método a ser testado
        UserEntity updatedUser = userService.update(userId, new UserEntity(null, existingUsername, "newPassword", UserEntity.Role.ROLE_ADMIN));

        // Assert
        // Verifica os resultados
        assertNotNull(updatedUser); // Verifica se o usuário atualizado não é nulo
        assertEquals("encodedNewPassword", updatedUser.getPassword()); // Verifica se a senha foi corretamente atualizada
        assertEquals(UserEntity.Role.ROLE_ADMIN, updatedUser.getRole()); // Verifica se a função do usuário foi corretamente atualizada
        verify(userRepository, times(1)).existsById(userId); // Verifica se existsById foi chamado uma vez com o ID correto
        verify(userRepository, times(1)).save(any(UserEntity.class)); // Verifica se save foi chamado uma vez com uma entidade de usuário
    }

    // Teste de falha na atualização de usuário (usuário não encontrado)
    @Test
    void testUpdateUser_Failure_UserNotFound() {
        // Arrange
        // Simula a inexistência do usuário com o ID especificado
        when(userRepository.existsById(1L)).thenReturn(false);

        // Assert
        // Verifica se uma exceção é lançada ao tentar atualizar um usuário inexistente
        assertThrows(UserNotFoundException.class, () -> {
            // Act
            userService.update(1L, new UserEntity(null, "user1", "newPassword", UserEntity.Role.ROLE_ADMIN));
        });
    }

    // Teste de exclusão de usuário bem-sucedida
    @Test
    void testDeleteUser_Success() {
        // Arrange
        // Usuário existente a ser excluído
        UserEntity existingUser = new UserEntity(1L, "user1", "password1", UserEntity.Role.ROLE_CLIENTE);
        // Configura o comportamento do mock para retornar esse usuário ao buscar pelo ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Act
        // Chama o método a ser testado
        userService.delete(1L);

        // Assert
        // Verifica se o método delete do repositório foi chamado com o usuário correto
        verify(userRepository, times(1)).delete(existingUser);
    }

    // Teste de falha na exclusão de usuário (usuário não encontrado)
    @Test
    void testDeleteUser_Failure_UserNotFound() {
        // Arrange
        // Simula a inexistência do usuário com o ID especificado
        when(userRepository.findById(1L)).thenThrow(UserNotFoundException.class);

        // Assert
        // Verifica se uma exceção é lançada ao tentar excluir um usuário inexistente
        assertThrows(UserNotFoundException.class, () -> {
            // Act
            userService.delete(1L);
        });
    }

    // Teste para encontrar usuário por ID
    @Test
    void testFindById() {
        // Arrange
        // Usuário existente
        UserEntity existingUser = new UserEntity(1L, "user1", "password1", UserEntity.Role.ROLE_CLIENTE);
        // Configura o comportamento do mock para retornar esse usuário ao buscar pelo ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // Act
        // Chama o método a ser testado
        Optional<UserEntity> result = userService.findById(1L);

        // Assert
        // Verifica se o usuário retornado é o esperado
        assertTrue(result.isPresent());
        assertEquals(existingUser, result.get());
    }

    // Teste para encontrar usuário por nome de usuário (sucesso)
    @Test
    void testFindByUsername_Success() {
        // Arrange
        // Usuário existente
        UserEntity existingUser = new UserEntity(1L, "user1", "password1", UserEntity.Role.ROLE_CLIENTE);
        // Configura o comportamento do mock para retornar esse usuário ao buscar pelo nome de usuário
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(existingUser));

        // Act
        // Chama o método a ser testado
        UserEntity result = userService.findByUsername("user1");

        // Assert
        // Verifica se o usuário retornado é o esperado
        assertEquals(existingUser, result);
    }

    // Teste para encontrar usuário por nome de usuário (falha - usuário não encontrado)
    @Test
    void testFindByUsername_Failure_UserNotFound() {
        // Arrange
        // Simula a inexistência do usuário com o nome especificado
        when(userRepository.findByUsername("unknownUser")).thenReturn(Optional.empty());

        // Assert
        // Verifica se uma exceção é lançada ao tentar encontrar um usuário inexistente
        assertThrows(EntityNotFoundException.class, () -> {
            // Act
            userService.findByUsername("unknownUser");
        });
    }

    // Teste para encontrar função do usuário por nome de usuário
    @Test
    void testFindByRolePerUsername() {
        // Arrange
        // Configura o comportamento do mock para retornar a função do usuário ao buscar pelo nome de usuário
        when(userRepository.FindByRolePerUsername("user1")).thenReturn(UserEntity.Role.ROLE_ADMIN);

        // Act
        // Chama o método a ser testado
        UserEntity.Role result = userService.FindByRolePerUsername("user1");

        // Assert
        // Verifica se a função do usuário retornado é a esperada
        assertEquals(UserEntity.Role.ROLE_ADMIN, result);
    }
}




