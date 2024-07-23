package freelanceplatform.controllers;

import freelanceplatform.controllers.util.RestUtils;
import freelanceplatform.dto.Mapper;
import freelanceplatform.dto.creation.UserCreation;
import freelanceplatform.dto.readUpdate.UserReadUpdate;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.model.Resume;
import freelanceplatform.model.User;
import freelanceplatform.model.security.UserDetails;
import freelanceplatform.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing users.
 * This controller provides endpoints for user CRUD operations and other user-related actions.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/users")
public class UserController {

    private final UserService userService;
    private final Mapper mapper;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the ResponseEntity with the user data or 404 if not found
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReadUpdate> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok(mapper.toReadUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user
     * @return the ResponseEntity with the user data or 404 if not found
     */
    @GetMapping(value = "/username/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserReadUpdate> getUserByUserName(@PathVariable String username) {
        final UserReadUpdate userReadUpdate = mapper.toReadUser(userService.findByUsername(username));
        return ResponseEntity.ok(userReadUpdate);
    }

    /**
     * Retrieves all users.
     *
     * @return an iterable of all user DTOs
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<UserReadUpdate> getAllUsers() {
        Iterable<User> users = userService.findAll();

        List<UserReadUpdate> userReadUpdates = new ArrayList<>();
        for (User user : users) {
            userReadUpdates.add(mapper.toReadUser(user));
        }

        return userReadUpdates;
    }

    /**
     * Signs up a new user.
     *
     * @param userCreation the user creation data transfer object
     * @return the ResponseEntity indicating the result of the operation
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signUp(@RequestBody UserCreation userCreation) {
        User user = mapper.toUser(userCreation);
        userService.save(user);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Retrieves the current authenticated user.
     *
     * @param auth the authentication object
     * @return the user DTO of the current user
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GUEST')")
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserReadUpdate getCurrent(Authentication auth) {
        assert auth.getPrincipal() instanceof UserDetails;
        return mapper.toReadUser(((UserDetails) auth.getPrincipal()).getUser());
    }

    /**
     * Updates a user's information.
     *
     * @param id              the ID of the user to update
     * @param userReadUpdateToUpdate the user data to update
     * @param auth            the authentication object
     * @return the ResponseEntity indicating the result of the operation
     */
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Integer id,
                                           @RequestBody UserReadUpdate userReadUpdateToUpdate, Authentication auth) {
        final User user = ((UserDetails) auth.getPrincipal()).getUser();

        if (!id.equals(userReadUpdateToUpdate.getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final User userToUpdate = userService.findById(id).orElse(null);
        if (!user.getId().equals(Objects.requireNonNull(userToUpdate).getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        userToUpdate.setFirstName(userReadUpdateToUpdate.getFirstName());
        userToUpdate.setLastName(userReadUpdateToUpdate.getLastName());
        userToUpdate.setEmail(userReadUpdateToUpdate.getEmail());

        userService.update(userToUpdate);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    /**
     * Deletes a user by an admin.
     *
     * @param id the ID of the user to delete
     * @return the ResponseEntity indicating the result of the operation
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUserByAdmin(@PathVariable Integer id) {
        return userService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Deletes the current authenticated user's account.
     *
     * @param auth the authentication object
     * @return the ResponseEntity indicating the result of the operation
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAccount(Authentication auth) {
        return Optional.ofNullable(((UserDetails) auth.getPrincipal()).getUser())
                .map(user -> {
                    userService.deleteById(user.getId());
                    HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/");
                    return new ResponseEntity<Void>(headers, HttpStatus.NO_CONTENT);
                })
                .orElseThrow(() -> new NotFoundException("User not found"));
    }


    /**
     * Retrieves the resume of the current authenticated user.
     *
     * @param auth the authentication object
     * @return the ResponseEntity with the resume or 404 if not found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/myResume")
    public ResponseEntity<Resume> getResume(Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        Resume resume = userService.getUsersResume(user);
        return ResponseEntity.ok(resume);
    }

    /**
     * Saves the resume of the current authenticated user.
     *
     * @param filename the filename of the resume
     * @param file     the resume file
     * @param auth     the authentication object
     * @return the ResponseEntity indicating the result of the operation
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/addResume")
    public ResponseEntity<Void> saveResume(@RequestParam("filename") String filename,
                                           @RequestParam("content") MultipartFile file,
                                           Authentication auth) throws IOException {
        User user = ((UserDetails) auth.getPrincipal()).getUser();

        userService.saveResume(filename, file.getBytes(), user);

        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/current");
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
