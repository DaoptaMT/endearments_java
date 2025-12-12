package mt.endearments.service.impl;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.dto.request.UserRequestDTO;
import mt.endearments.dto.response.UserResponseDTO;
import mt.endearments.enums.ApiException;
import mt.endearments.enums.ErrorCode;
import mt.endearments.enums.RoleType;
import mt.endearments.mapper.UserMapper;
import mt.endearments.model.Role;
import mt.endearments.model.RoleUser;
import mt.endearments.model.RoleUserId;
import mt.endearments.model.User;
import mt.endearments.repository.user.RoleRepository;
import mt.endearments.repository.user.UserRepository;
import mt.endearments.repository.user.UserRoleRepository;
import mt.endearments.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    UserRoleRepository userRoleRepository;
    RoleRepository roleRepository;

    @Override
    public UserResponseDTO register(UserRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(ErrorCode.EMAIL_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        if (user.getOtpAttempts() == null) {
            user.setOtpAttempts(0);
        }
        if (user.getOtpExpiresAt() == null) {
            user.setOtpExpiresAt(Instant.now().plusSeconds(300));
        }
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        User savedUser = userRepository.save(user);

        List<Role> resolvedRoles = new ArrayList<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            resolvedRoles.addAll(resolveRoles(request.getRoles()));
        }

        Role defaultUserRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND));
        if (resolvedRoles.stream().noneMatch(r -> r.getName().equalsIgnoreCase("USER"))) {
            resolvedRoles.add(defaultUserRole);
        }

        for (Role role : resolvedRoles) {
            RoleUserId roleUserId = new RoleUserId();
            roleUserId.setUserId(savedUser.getId());
            roleUserId.setRoleId(role.getId());

            RoleUser roleUser = new RoleUser();
            roleUser.setId(roleUserId);
            roleUser.setUser(savedUser);
            roleUser.setRole(role);
            roleUser.setCreatedAt(Instant.now());
            roleUser.setUpdatedAt(Instant.now());

            userRoleRepository.save(roleUser);
        }

        UserResponseDTO userMap = userMapper.toUserResponseDTO(savedUser);
        userMap.setRoles(resolvedRoles.stream()
                .map(Role::getName)
                .collect(Collectors.joining(", ")));

        return userMap;
    }

    @Override
    public Object getAllUser(int page, int pageSize) {
        return null;
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void associateRolesWithUser(User savedUser, List<Role> resolvedRoles) {
        resolvedRoles.forEach(role -> {
            RoleUser userHasRole = new RoleUser();
            userHasRole.setUser(savedUser);
            userHasRole.setRole(role);
            userRoleRepository.save(userHasRole);
        });
    }

    private List<Role> resolveRoles(@NotEmpty(message = "ROLE_INVALID") List<RoleType> roles) {
        return roles.stream()
                .map(roleName -> roleRepository.findByName(String.valueOf(roleName))
                        .orElseThrow(() -> new ApiException(ErrorCode.ROLE_NOT_FOUND)))
                .collect(Collectors.toList());
    }
}
