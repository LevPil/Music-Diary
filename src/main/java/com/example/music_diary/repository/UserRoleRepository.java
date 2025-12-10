package com.example.music_diary.repository;

import com.example.music_diary.models.entity.Role;
import com.example.music_diary.models.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findRoleByName(Roles role);
    Optional<Role> findByName(Roles name);
}
