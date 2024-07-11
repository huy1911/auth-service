package com.lpb.mid.repo;


import com.lpb.mid.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    Users findByUserNameAndStatus(String userName,String status);
}
