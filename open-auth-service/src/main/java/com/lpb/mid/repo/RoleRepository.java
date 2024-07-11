package com.lpb.mid.repo;


import com.lpb.mid.entity.Permision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Permision, String> {
    @Query(value = "select permision.* from users    join user_permision on users.id = user_permision.user_id\n" +
            "    join permision on permision.id = user_permision.role_id where users.id = :userId",
            nativeQuery = true)
    List<Permision> listRoleByUser(@Param("userId") String userId);
    Permision findFirstByRoleName(String roleName);
}
