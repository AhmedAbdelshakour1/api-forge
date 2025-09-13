package com.example.apiforge.service;

import com.example.apiforge.dto.userDTO.UserRequestDto;
import com.example.apiforge.dto.userDTO.UserResponseDto;
import com.example.apiforge.entity.User;
import com.example.apiforge.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByUserName(userRequestDto.getUsername()) ||  userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException("Username or Email already exists");
        }
        User user = new User();
        user.setUserName(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        User newUser = userRepository.save(user);
        return convertToResponseDto(newUser);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return convertToResponseDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToResponseDto).toList();
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUserName(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        User updatedUser = userRepository.save(user);
        return convertToResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User  user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    private UserResponseDto convertToResponseDto(User newUser) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUsername(newUser.getUserName());
        dto.setEmail(newUser.getEmail());
        dto.setId(newUser.getId());
        dto.setCreatedAt(newUser.getCreatedAt());
        dto.setUpdatedAt(newUser.getUpdatedAt());
        return dto;
    }
}
