package br.challege.user.management.system.domain;

import br.challege.user.management.system.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static br.challege.user.management.system.common.USER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    public void afterEach(){USER.setId(null);}


    @Test
    public void createdPlanet_WithValidData_ReturnsUser(){
        UserEntity user =userRepository.save(USER);

        UserEntity sut = testEntityManager.find(UserEntity.class,user.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getUsername()).isEqualTo(USER.getUsername());
        assertThat(sut.getPassword()).isEqualTo(USER.getPassword());
        assertThat(sut.getRole()).isEqualTo(USER.getRole());
    }
}
