package com.und.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.und.model.security.User;

/**
 * Created by shiv on 21/07/17.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
