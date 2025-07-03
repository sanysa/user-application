package com.bitlab.project.service;

import com.bitlab.project.model.dto.UserDto;
import com.bitlab.project.model.entity.User;

import java.util.List;

public interface UserService {

    void save(UserDto userDto);

    List<User> getUsersByEmailAndUsername(String email, String username) ;

     List<User> getAllUsers();
}
