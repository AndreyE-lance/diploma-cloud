package com.elantsev.netology.diplomacloud.repository;

import com.elantsev.netology.diplomacloud.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthoritiesRepository extends JpaRepository<Authority, String> {
    List<Authority> findAuthoritiesById_Username(String username);
}
