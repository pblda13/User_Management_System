package br.challege.user.management.system.web.controller;

import br.challege.user.management.system.domain.UserEntity;
import br.challege.user.management.system.domain.exception.UserNotFoundException;
import br.challege.user.management.system.dto.UserDTO;
import br.challege.user.management.system.dto.mapper.UserMapper;
import br.challege.user.management.system.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserEntity userEntity = userMapper.convertToEntity(userDTO);
        userEntity.setPassword(userDTO.getPassword());
        UserEntity createdUser = userService.create(userEntity);
        UserDTO responseDTO = userMapper.convertToDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listAllUsers() {
        List<UserEntity> userList = userService.listAll();
        List<UserDTO> userDTOList = userList.stream()
                .map(userMapper::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserEntity existingUser = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));

        // Atualizando os campos do usuário existente com os valores do DTO
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setPassword(userDTO.getPassword());
        existingUser.setRole(UserEntity.Role.valueOf(String.valueOf(userDTO.getRole())));

        // Chama o serviço para atualizar o usuário
        UserEntity updatedUser = userService.update(id, existingUser);

        // Converte a entidade atualizada para DTO e retorna
        UserDTO responseDTO = userMapper.convertToDTO(updatedUser);
        return ResponseEntity.ok(responseDTO);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserEntity user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User " + id + " not found"));
        UserDTO userDTO = userMapper.convertToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        UserEntity user = userService.findByUsername(username);
        UserDTO userDTO = userMapper.convertToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

}



