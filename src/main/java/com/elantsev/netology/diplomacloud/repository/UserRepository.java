package com.elantsev.netology.diplomacloud.repository;

import com.elantsev.netology.diplomacloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String name);
}
