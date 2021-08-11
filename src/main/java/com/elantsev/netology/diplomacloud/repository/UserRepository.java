package com.elantsev.netology.diplomacloud.repository;

import com.elantsev.netology.diplomacloud.model.FileInCloud;
import com.elantsev.netology.diplomacloud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String name);
}
