package duckie.example.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import duckie.example.backend.dto.UserPatchRequest;
import duckie.example.backend.dto.UserRequest;
import duckie.example.backend.dto.UserResponse;

public interface IUserService {
    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse create(UserRequest request);
    ResponseEntity<UserResponse> update(Long id, UserRequest request);
    ResponseEntity<UserResponse> patch(Long id, UserRequest request);
    ResponseEntity<UserResponse> patchUpdate(Long id, UserPatchRequest request);
    void delete(Long id);
}