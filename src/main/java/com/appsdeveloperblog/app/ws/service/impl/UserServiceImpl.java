package com.appsdeveloperblog.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.appsdeveloperblog.app.ws.enitity.UserEntity;
import com.appsdeveloperblog.app.ws.io.repositories.UserRepository;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record Already Exists");
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);
		String publicUserId = utils.generateUserId(30);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userEntity.setUserId(publicUserId);
		
		UserEntity storedUserDetails = userRepository.save(userEntity);
		

		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}
	
	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		return returnValue;
		
		
	}

	@Override
	public UserDto getUserByUserId(String id) {
		UserDto userDto = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if(userEntity == null) throw new UsernameNotFoundException(id);
		BeanUtils.copyProperties(userEntity, userDto);



		return userDto;
	}

	@Override
	public UserDto updateUser(String id, UserDto user) {
		UserDto userDto = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(id);
		if(userEntity == null) throw new UsernameNotFoundException(id);
		
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());

		UserEntity updatedUserEntity = userRepository.save(userEntity);
		BeanUtils.copyProperties(updatedUserEntity, userDto);
		return userDto;
	}

	@Override
	public void deleteUser(String id) {
		UserEntity userEntity = userRepository.findByUserId(id);
		if(userEntity == null) throw new UsernameNotFoundException(id);
		
		userRepository.deleteById(userEntity.getId());
		
		return;
	}

}
