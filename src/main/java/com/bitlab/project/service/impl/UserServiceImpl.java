package com.bitlab.project.service.impl;

import com.bitlab.project.mapper.UserMapper;
import com.bitlab.project.model.dto.UserDto;
import com.bitlab.project.model.entity.User;
import com.bitlab.project.repository.UserRepository;
import com.bitlab.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public void save(UserDto userDto) {
        User user = userMapper.toUser(userDto);

        userRepository.save(user);
    }

    @Override
    public List<User> getUsersByEmailAndUsername(String email, String username) {
        return List.of();
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }
}