package com.ms.controller;

import com.ms.dto.request.UserRequestDto;
import com.ms.dto.response.ApiResponse;
import com.ms.dto.response.ResponseUtil;
import com.ms.dto.response.UserResponseDto;
import com.ms.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks class as REST controller
@RequestMapping("/api/users") // Base URL
@RequiredArgsConstructor // Lombok constructor injection
public class UserController {

    private final UserService userService; // Inject service

    // CREATE USER
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @RequestBody UserRequestDto request) {

        UserResponseDto response = userService.createUser(request);

        return ResponseUtil.success(
                HttpStatus.CREATED,
                "User created successfully",
                response
        );
    }

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {

        List<UserResponseDto> users = userService.getAllUsers();

        return ResponseUtil.success(
                HttpStatus.OK,
                "Users fetched successfully",
                users
        );
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @PathVariable Long id) {

        UserResponseDto user = userService.getUserById(id);

        return ResponseUtil.success(
                HttpStatus.OK,
                "User fetched successfully",
                user
        );
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDto request) {

        UserResponseDto updatedUser = userService.updateUser(id, request);

        return ResponseUtil.success(
                HttpStatus.OK,
                "User updated successfully",
                updatedUser
        );
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseUtil.success(
                HttpStatus.OK,
                "User deleted successfully",
                null
        );
    }

}