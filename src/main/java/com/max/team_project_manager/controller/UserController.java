package com.max.team_project_manager.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.max.team_project_manager.dto.CreateUserRequest;
import com.max.team_project_manager.dto.UserResponse;
import com.max.team_project_manager.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

	@PostMapping
	public UserResponse createUser(
			@Valid
			@RequestBody
			CreateUserRequest request
	) {
		return userService.createUser(request);
	}
}
