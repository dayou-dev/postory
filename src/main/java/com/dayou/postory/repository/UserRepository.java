package com.dayou.postory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dayou.postory.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailAndPassword(String email, String password);

	boolean existsByEmail(String email);
}
