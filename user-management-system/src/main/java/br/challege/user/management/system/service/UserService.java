package br.challege.user.management.system.service;

import br.challege.user.management.system.domain.UserEntity;
import br.challege.user.management.system.domain.exception.UserNotFoundException;
import br.challege.user.management.system.domain.exception.UsernameUniqueViolationException;
import br.challege.user.management.system.producer.KafkaProducer;
import br.challege.user.management.system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer kafkaProducer;


    @Transactional
    public UserEntity create(UserEntity user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            kafkaProducer.sendMessage("Usuario cadastrado com sucesso");

            return user;
        } catch (DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já cadastrado", user.getUsername()));
        }

    }

    public List<UserEntity> listAll() {
        List<UserEntity> list = userRepository.findAll();
        return list;
    }

    public UserEntity update(Long id, UserEntity user) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User " + id + "not found ");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void delete(Long id) {
        UserEntity delete = userRepository.findById(id).get();
        userRepository.delete(delete);

    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }


    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario username %s não encontrado", username))
        );
    }

    public UserEntity.Role FindByRolePerUsername(String username) {
        return userRepository.FindByRolePerUsername(username);
    }
}

