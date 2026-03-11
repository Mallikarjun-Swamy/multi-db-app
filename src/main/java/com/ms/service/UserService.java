package com.ms.service;

import com.ms.dto.request.UserRequestDto;
import com.ms.dto.response.UserResponseDto;
import com.ms.mapper.UserMapper;
import com.ms.model.postgres.User;
import com.ms.repository.postgres.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserResponseDto createUser(UserRequestDto dto){
        User user = mapper.toEntity(dto);
        return mapper.toDto(userRepository.save(user));
    }

    public List<UserResponseDto> getAllUsers(){
        return mapper.toDtoList(userRepository.findAll());
    }

    public UserResponseDto getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not Found with Id" + id));
        return mapper.toDto(user);
    }


    public UserResponseDto updateUser(Long id,UserRequestDto dto){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not Found with Id" + id));

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        return mapper.toDto(userRepository.save(user)
        );
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User Not Found with Id" + id));
       userRepository.deleteById(id);
    }

}
