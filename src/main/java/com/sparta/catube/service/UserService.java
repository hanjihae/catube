package com.sparta.catube.service;

import com.sparta.catube.dto.UserDto;
import com.sparta.catube.entity.User;
import com.sparta.catube.repository.UserRepository;
import com.sparta.catube.security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
                       JwtTokenUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUserType(userDto.getUserType());
        user.setUserName(userDto.getUserName());
        user.setUserNickname(userDto.getUserNickname());
        user.setUserEmail(userDto.getUserEmail());
        user.setUserPw(passwordEncoder.encode(userDto.getUserPw()));
        user.setUserImgUrl(userDto.getUserImgUrl());
        user.setUserLoginMethod(userDto.getUserLoginMethod());
        user.setUserStatus(userDto.getUserStatus());
        return userRepository.save(user);
    }

    public String loginUser(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                userDto.getUserEmail(), userDto.getUserPw()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.getUserEmail());
        return jwtTokenUtil.generateToken(userDetails);
    }

}
