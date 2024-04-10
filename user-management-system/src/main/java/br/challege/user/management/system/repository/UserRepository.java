package br.challege.user.management.system.repository;

import br.challege.user.management.system.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String usermane);

    @Query("select u.role from UserEntity u where u.username like :username")
    UserEntity.Role FindByRolePerUsername(String username);
}
