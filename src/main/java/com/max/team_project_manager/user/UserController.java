package com.max.team_project_manager.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

	@GetMapping("/{id}")
	public UserResponse getById(@PathVariable Long id) {
		return userService.getById(id);
	}

	@PatchMapping("/me")
	public UserResponse updateProfile(
			@Valid
			@RequestBody
			UpdateUserRequest request
	) {
		return userService.updateProfile(request);
	}
}
