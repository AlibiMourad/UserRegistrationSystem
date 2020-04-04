package com.apress.ravi.rest;


import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apress.ravi.dto.UsersDTO;
import com.apress.ravi.repository.UserJpaRepository;

import exception.CustomErrorType;

@RestController
@RequestMapping("/api/user")
public class UserRegistrationRestController {

	public static final Logger logger = LoggerFactory.getLogger(UserRegistrationRestController.class);
	
	private UserJpaRepository userJpaRepository;

	@Autowired
	public void setUserJpaRepository(UserJpaRepository userJpaRepository) {
		this.userJpaRepository = userJpaRepository;
	}
	
	// Get All USER : //
	@GetMapping("/")
	public ResponseEntity<List<UsersDTO>> listAllusers(){
		List<UsersDTO> users = userJpaRepository.findAll();
		if(users.isEmpty()) {
			return new ResponseEntity<List<UsersDTO>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<UsersDTO>>(users, HttpStatus.OK);
	}
	
	// Add new USER : //
	@PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersDTO> createUser(@Valid @RequestBody final UsersDTO user) {
		logger.info("Creating User : {}", user);
		if(userJpaRepository.findByName(user.getName()) != null) {
			logger.error("Unable to create. A User with name {} already exist",
					user.getName());
			return new ResponseEntity<UsersDTO>(new CustomErrorType(
					"Unable to create new User. A user with name " + 
					user.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		userJpaRepository.save(user);
		return new ResponseEntity<UsersDTO>(user, HttpStatus.CREATED);
	}
	
	// Get USER by ID : //
	@GetMapping("/{id}")
	public ResponseEntity<UsersDTO> getUserById(@PathVariable("id") final Long id){
		Optional<UsersDTO> user = userJpaRepository.findById(id);
		if(! user.isPresent()) {
			 return new ResponseEntity<UsersDTO>(new CustomErrorType("User with id : "
					 	 + id + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UsersDTO>(user.get(), HttpStatus.OK);			
	}
	
	// Update USER by ID : //
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UsersDTO> updateUser(@PathVariable("id") final Long id,
			@RequestBody UsersDTO user) {
		Optional<UsersDTO> currentUser  = userJpaRepository.findById(id);
		if(! currentUser.isPresent()) {
			return new ResponseEntity<UsersDTO>(new CustomErrorType("Unable to upate. "
					+ "User with id : " + id + " not found."), HttpStatus.NOT_FOUND);
		}
		currentUser.get().setName(user.getName());
		currentUser.get().setAddress(user.getAddress());
		currentUser.get().setEmail(user.getEmail());
		userJpaRepository.saveAndFlush(currentUser.get());
		return new ResponseEntity<UsersDTO>(currentUser.get(), HttpStatus.OK);
	}
	
	// Delete USER by ID : //
	@DeleteMapping("/{id}")
	public ResponseEntity<UsersDTO> deleteUser(@PathVariable("id") final Long id) {
		Optional<UsersDTO> user = userJpaRepository.findById(id);
		if(! user.isPresent()) {
			return new ResponseEntity<UsersDTO>(new CustomErrorType("Unable to delete. "
					+ "User with id :" + id +" not found."), HttpStatus.NOT_FOUND);	
		}
		userJpaRepository.deleteById(id);
		return new ResponseEntity<UsersDTO>(HttpStatus.NO_CONTENT);
	}
}
