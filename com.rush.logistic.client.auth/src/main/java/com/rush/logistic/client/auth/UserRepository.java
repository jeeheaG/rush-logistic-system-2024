package com.rush.logistic.client.auth;

import com.rush.logistic.client.auth.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
