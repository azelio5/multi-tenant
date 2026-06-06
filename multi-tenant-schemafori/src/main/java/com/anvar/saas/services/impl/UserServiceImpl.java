package com.anvar.saas.services.impl;

import com.anvar.saas.common.PageResponse;
import com.anvar.saas.config.TenantContext;
import com.anvar.saas.entities.Tenant;
import com.anvar.saas.entities.User;
import com.anvar.saas.entities.UserRole;
import com.anvar.saas.exceptions.DuplicateResourceException;
import com.anvar.saas.exceptions.InvalidRequestException;
import com.anvar.saas.mappers.UserMapper;
import com.anvar.saas.repositories.UserRepository;
import com.anvar.saas.requests.UserRequest;
import com.anvar.saas.responses.UserResponse;
import com.anvar.saas.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();

        //validate if username exists
        if (this.userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        //validate if email exists
        if (this.userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        //validate role (cannot be PLATFORM_ADMIN)
        if (request.getRole() == null || request.getRole().equals(UserRole.ROLE_PLATFORM_ADMIN)) {
            throw new InvalidRequestException("Platform admin cannot be created");

        }

        final User user = this.userMapper.toEntity(request);
        user.setTenant(Tenant.builder().id(tenantId).build());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        this.userRepository.save(user);
        log.info("User created: {}", user);

    }

    @Override
    public void updateUser(final String userId, final UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Updating user for tenant: {}", tenantId);

        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        // check if username is being changed and if it is already taken
        if (!user.getUsername().equals(request.getUsername()) && this.userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        // check if email is being changed and if it is already taken
        if (!user.getEmail().equals(request.getEmail()) && this.userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // validate role (cannot be PLATFORM_ADMIN)
        if (request.getRole() == UserRole.ROLE_PLATFORM_ADMIN) {
            throw new InvalidRequestException("Role cannot be PLATFORM_ADMIN");
        }

        // update user details
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        this.userRepository.save(user);
        log.info("User updated successfully");
    }

    @Override
    public void deleteUser(String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Deleting user for tenant: {}", tenantId);
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Used does not exists"));
        if (!user.getTenantId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        //soft delete - only flag
        user.setDeleted(true);
        log.info("User deleted successfully");
        this.userRepository.save(user);
    }

    @Override
    public UserResponse getUserById(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        final String tenantId = TenantContext.getCurrentTenant();
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<User> userPage = this.userRepository.findAllByTenantId(tenantId, pageRequest);
        final Page<UserResponse> userResponses = userPage.map(userMapper::toResponse);

        return PageResponse.of(userResponses);
    }

    @Override
    public void enableUser(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(true);
        this.userRepository.save(user);
        log.info("User enabled successfully");
    }

    @Override
    public void disableUser(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(false);
        this.userRepository.save(user);
        log.info("User disabled successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));
    }
}
