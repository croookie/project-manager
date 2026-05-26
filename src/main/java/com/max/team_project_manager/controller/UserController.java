package com.max.team_project_manager.controller;

import com.max.team_project_manager.dto.CreateUserRequest;
import com.max.team_project_manager.dto.UserResponse;
import com.max.team_project_manager.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
	/*
	@GetMapping("/users")
	public List<User> getUsers() {
		return userRepository.getAllUsers;
	}
	*/

    /*

    @PostMapping("/auth/login")
    @PostMapping("/auth/signup")

    @GetMapping("/projects")
    returns list of project

    @PostMapping("/projects")
    returns created project

    @GetMapping("/projects/{project_id}")
    returns project

    @GetMapping("/projects/{project_id}/goals")
    returns list of goals

    @PostMapping("/projects{project_id}/goals")
    returns created goal

    @GetMapping("/projects/{project_id}/team_members")
    returns list of team members

    @PostMapping("/projects{project_id}/team_members")
    returns created team member
     */
}
