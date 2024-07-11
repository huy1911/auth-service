package com.lpb.mid.repo;

import com.lpb.mid.entity.UserPermision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserPermision, String> {
}
