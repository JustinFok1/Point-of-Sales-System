package com.PosSystem.POS.System.Dao;

import com.PosSystem.POS.System.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface UserRepo extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByEmployeeCode(String employeeCode);
    Optional<User> findByEmail(String email);
}
