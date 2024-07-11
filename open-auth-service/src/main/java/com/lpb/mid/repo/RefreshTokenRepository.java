package com.lpb.mid.repo;

import com.lpb.mid.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, String> {


}
