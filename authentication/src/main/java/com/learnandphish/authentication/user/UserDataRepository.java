package com.learnandphish.authentication.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Robin Lafontaine
 */
@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {

    List<UserData> findByEmail(String email);

}