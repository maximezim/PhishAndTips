package com.learnandphish.authentication.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserScanRepository extends JpaRepository<ScanResult, String> {

    @Query(value = "SELECT s.* FROM spiderfoot_scans s WHERE s.target = :email", nativeQuery = true)
    ScanResult findScanByEmail(@Param("email") String email);

}
