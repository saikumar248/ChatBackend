package com.chat.app.service;

import java.util.List; 

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.chat.app.dto.UserDto;
import com.chat.app.exception.DuplicateException;
import com.chat.app.exception.ResourceNotFound;
import com.chat.app.model.User;
import com.chat.app.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	public User registerUser(User user) {
		if (userRepository.findByEmail(user.getEmail()) != null
				|| userRepository.findByPhoneNumber(user.getPhoneNumber()) != null) {
			throw new DuplicateException("User already exists!");
		}
		return userRepository.save(user);
	}

	public User loginUser(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user == null || !user.getPassword().equals(password)) {
			throw new ResourceNotFound("Invalid credentials!");
		}
		return user;
	}

	public ResponseEntity<List<User>> fetchAll() {
		List<User> users = userRepository.findAll();
		return ResponseEntity.ok(users);
	}
	

	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("User not found with ID: " + id));
		userRepository.delete(user);
	}

	public User updateUser(Long id, User updatedUser) {
		User existingUser = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFound("User not found with ID: " + id));
		existingUser.setLastName(updatedUser.getLastName());
		existingUser.setFirstName(updatedUser.getFirstName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
		existingUser.setPassword(updatedUser.getPassword());
		return userRepository.save(existingUser);
	}
	
	
	 public User fetchByEmail(String email) {
	        return userRepository.findByEmail(email);  // This should not be null if the injection works
	    }
	
	
	public User updateUserByEmail(String email, User updatedUser) {
	    User existingUser = userRepository.findByEmail(email);
	    		
	   	if (existingUser == null ) {
	    			throw new ResourceNotFound("Invalid credentials!");
	    }
	           
	    existingUser.setLastName(updatedUser.getLastName());
	    existingUser.setFirstName(updatedUser.getFirstName());
	    existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
	    existingUser.setPassword(updatedUser.getPassword());
	    return userRepository.save(existingUser);
	}

	public User updateUserPasswordByEmail(String email, String newPassword) {
	    User existingUser = userRepository.findByEmail(email);

	    if (existingUser == null) {
	        throw new ResourceNotFound("Invalid credentials!");
	    }

	    existingUser.setPassword(newPassword);
	    return userRepository.save(existingUser);
	}


	public ResponseEntity<List<UserDto>> fetch() {
		List<User> data = userRepository.findAll();

		User existedData = (User) data;
		@SuppressWarnings("unchecked")
		List<UserDto> userDto = (List<UserDto>) modelMapper.map(existedData, UserDto.class);
		return ResponseEntity.status(HttpStatus.OK).body(userDto);
	}
	
	
	

}
