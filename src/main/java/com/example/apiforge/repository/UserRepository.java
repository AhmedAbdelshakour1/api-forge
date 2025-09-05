package com.example.apiforge.repository;

import com.example.apiforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.userName = :username OR u.email = :email")
    Optional<User> findByUserNameOrEmail(@Param("username") String username, @Param("email") String email);
}
