package com.learnandphish.authentication;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Robin Lafontaine
 */
public interface UserRepository extends JpaRepository<User, Long> {

}