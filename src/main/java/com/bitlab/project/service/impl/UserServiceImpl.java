package com.bitlab.project.service.impl;

import com.bitlab.project.model.dto.UserDto;
import com.bitlab.project.model.entity.User;
import com.bitlab.project.repository.UserRepository;
import com.bitlab.project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public void save(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.email());
        user.setUsername(userDto.username());

        userRepository.save(user);
    }
}
