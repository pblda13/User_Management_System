package br.challege.user.management.system.service;

import br.challege.user.management.system.domain.User;
import br.challege.user.management.system.domain.exception.UserNotFoundException;
import br.challege.user.management.system.dto.UserDTO;
import br.challege.user.management.system.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public void create(UserDTO userDTO) {
        User newUser = new User(userDTO);
        userRepository.save(newUser);
    }

    public List<UserDTO> listAll() {
        List<User> list = userRepository.findAll();
        return list.stream().map(UserDTO::new).toList();
    }

    public UserDTO update(Long id, UserDTO userDTO) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User " + id + "not found ");
        }
        User updateUser = new User(userDTO);
        return new UserDTO(userRepository.save(updateUser));
    }

    public void delete(Long id) {
        User delete = userRepository.findById(id).get();
        userRepository.delete(delete);

    }

    public UserDTO findById(Long id){

        return new UserDTO(userRepository.findById(id).get());
    }

    public UserDTO findByUsername(String username){
        return new UserDTO(userRepository.findByUsername(username).get());
}
}
