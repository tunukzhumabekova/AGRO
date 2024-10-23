package com.agro.service.impl;

import com.agro.config.JwtService;
import com.agro.exception.AuthenticationException;
import com.agro.model.request.SignIn;
import com.agro.model.request.SignUp;
import com.agro.model.response.Authentication;
import com.agro.public_.tables.records.UserInfosRecord;
import com.agro.public_.tables.records.UsersRecord;
import com.agro.repository.UserInfoRepository;
import com.agro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements com.agro.service.AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, JwtService jwtService,
                                     PasswordEncoder passwordEncoder, UserInfoRepository userInfoRepository
                                     ) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userInfoRepository = userInfoRepository;
    }
    @Override
    public Authentication signUp(SignUp signUp) {
        if (userInfoRepository.existsByName(signUp.username())) {
            throw new AuthenticationException("username already exists");
        }
        UserInfosRecord newUserInfo = new UserInfosRecord();
        newUserInfo.setUsername(signUp.username());
        newUserInfo.setPassword(passwordEncoder.encode(signUp.password()));
        newUserInfo.setRole(signUp.role());
        int userInfoId = userInfoRepository.save(newUserInfo);

        UsersRecord newUser = new UsersRecord();
        newUser.setUserInfoId(userInfoId);
        int userId = userRepository.save(newUser, userInfoId);

        var jwtToken = jwtService.generateToken(newUserInfo);

        return new Authentication(
                userId,
                newUserInfo.getUsername(),
                jwtToken,
                newUserInfo.getRole()
        );
    }

    @Override
    public Authentication signIn(SignIn signIn) {
        UserInfosRecord userInfo = userInfoRepository.findByUsername(signIn.username());

        if (userInfo == null) {
            throw new AuthenticationException("User not found");
        }

        if (!passwordEncoder.matches(signIn.password(), userInfo.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        UsersRecord user = userRepository.findByUserInfoId(userInfo.getId());

        String jwtToken = jwtService.generateToken(userInfo);
        return new Authentication(
                user.getId(),
                userInfo.getUsername(),
                jwtToken,
                userInfo.getRole()

        );
    }
}
