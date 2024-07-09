package ru.hse.moms.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.hse.moms.entity.Diary;
import ru.hse.moms.entity.Page;
import ru.hse.moms.entity.User;
import ru.hse.moms.exception.EmailAlreadyExistsException;
import ru.hse.moms.exception.UserNotFoundException;
import ru.hse.moms.exception.UsernameAlreadyExistsException;
import ru.hse.moms.exception.WrongPasswordException;
import ru.hse.moms.mapper.UserMapper;
import ru.hse.moms.repository.RewardRepository;
import ru.hse.moms.repository.RoleRepository;
import ru.hse.moms.repository.UserRepository;
import ru.hse.moms.request.GetUserRequest;
import ru.hse.moms.request.SignInRequest;
import ru.hse.moms.request.SignUpRequest;
import ru.hse.moms.request.UpdateUserRequest;
import ru.hse.moms.response.JwtResponse;
import ru.hse.moms.response.UserResponse;
import ru.hse.moms.security.AuthUtils;
import ru.hse.moms.security.PasswordEncoderConfig;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RewardRepository rewardRepository;


    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(String.format("User %s not found", username)));
    }

    public JwtResponse signUp(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    String.format("User %s already exists", signUpRequest.getUsername()));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EmailAlreadyExistsException(
                    String.format("Email %s already exists", signUpRequest.getEmail()));
        }
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoderConfig.getPasswordEncoder().encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .role(roleRepository.findByRoleName("USER").orElseThrow(() ->
                        new RuntimeException("Create Roles! it tries to find USER role in db")))
                .name(signUpRequest.getName())
                .diary(new Diary())
                .dateOfBirth(signUpRequest.getDateOfBirth())
                .balance(0)
                .build();
        User savedUser = userRepository.saveAndFlush(user);
        return JwtResponse.builder().jwt(jwtService.generateToken(savedUser)).build();
    }

    public JwtResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() ->
                new UserNotFoundException(String.format("User %s not found", signInRequest.getUsername())));
        if (passwordEncoderConfig.getPasswordEncoder().matches(signInRequest.getPassword(), user.getPassword())) {
            return JwtResponse.builder().jwt(jwtService.generateToken(user)).build();
        }
        throw new WrongPasswordException("Wrong password");
    }

    public UserResponse getUser(GetUserRequest getUserRequest) throws Exception {
        User user = null;
        if (getUserRequest.getId() != null) {
            user = userRepository.findById(getUserRequest.getId()).orElseThrow(() -> new UserNotFoundException(
                    String.format("User %s not found", getUserRequest.getId())));
        } else if (getUserRequest.getUsername() != null) {
            user = userRepository.findByUsername(getUserRequest.getUsername()).orElseThrow(() ->
                    new UserNotFoundException(String.format("User %s not found", getUserRequest.getUsername())));
        } else if (getUserRequest.getEmail() != null) {
            user = userRepository.findByEmail(getUserRequest.getEmail()).orElseThrow(() ->
                    new UserNotFoundException(String.format("User %s not found", getUserRequest.getEmail())));
        }
        if (user == null) {
            throw new BadRequestException("Empty");
        }
        return userMapper.makeUserResponse(user);
    }

    public UserResponse getCurrentUser() {
        Long userId = AuthUtils.getCurrentId();
        assert userId != null;
        return userMapper.makeUserResponse(
                userRepository.findById(userId).orElseThrow(
                        () -> new RuntimeException(
                                String.format("User with id %d is registered but not found in database", userId))
                )
        );
    }

    public UserResponse updateUser(UpdateUserRequest updateUserRequest) {
        Long userId = AuthUtils.getCurrentId();
        assert userId != null;
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %d is registered but not found in database", userId)));
        if (updateUserRequest.getPassword() != null) {
            if (!passwordEncoderConfig.getPasswordEncoder()
                    .matches(updateUserRequest.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoderConfig.getPasswordEncoder().encode(updateUserRequest.getPassword()));
            } else {
                throw new WrongPasswordException("Passwords should be different");
            }
        }
        if (updateUserRequest.getEmail() != null) {
            if (!updateUserRequest.getEmail().equals(user.getEmail())) {
                user.setEmail(updateUserRequest.getEmail());
            } else {
                throw new EmailAlreadyExistsException(
                        String.format("Email: %s already exist", updateUserRequest.getEmail()));
            }
        }
        return userMapper.makeUserResponse(userRepository.saveAndFlush(user));
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;

    }

    public UserResponse addReward(Long rewardId) {
        Long userId = AuthUtils.getCurrentId();
        assert userId != null;
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %d is registered but not found in database", userId)));
        user.getRewards().add(rewardRepository.findById(rewardId).orElseThrow(
                () -> new RuntimeException(String.format("Reward with id: %s not found", rewardId))));
        return userMapper.makeUserResponse(userRepository.saveAndFlush(user));
    }
    public void addPageToDiary(Page page) {
        Long userId = AuthUtils.getCurrentId();
        assert userId != null;
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                String.format("User with id %d is registered but not found in database", userId)));
        user.getDiary().getPages().add(page);
        userRepository.saveAndFlush(user);
    }
}
