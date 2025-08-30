package com.skillswap.repository;

import com.skillswap.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // This custom method will be used to find a role by its name.
    Optional<Role> findByName(String name);
}
