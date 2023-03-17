package com.cts.tanmoy.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.tanmoy.entity.AuthenticationRequest;

@Repository
public interface UserRepository extends JpaRepository<AuthenticationRequest,String> {

}
