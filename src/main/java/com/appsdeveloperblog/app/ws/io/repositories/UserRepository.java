package com.appsdeveloperblog.app.ws.io.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.appsdeveloperblog.app.ws.enitity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
//	want each user to be unique by email address
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);

}
