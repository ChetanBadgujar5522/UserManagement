package com.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.response.MessageResponse;
import com.project.dto.response.UserResponse;
import com.project.entity.User;
import com.project.exception.ResourceNotFoundException;
import com.project.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/getUsers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		List<UserResponse> users = userRepository.findAll().stream()
				.map(user -> new UserResponse(user.getId(), user.getEmail(), user.getRoles()))
				.collect(Collectors.toList());
		return ResponseEntity.ok(users);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or @userSecurity.hasUserId(authentication, #id)")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return ResponseEntity.ok(new UserResponse(user.getId(), user.getEmail(), user.getRoles()));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MessageResponse> deleteUser(@PathVariable Long id) {
		if (!userRepository.existsById(id)) {
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
		return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
	}
}
