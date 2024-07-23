package freelanceplatform.controllers;

import freelanceplatform.dto.Mapper;
import freelanceplatform.dto.creation.SolutionCreation;
import freelanceplatform.dto.readUpdate.SolutionReadUpdate;
import freelanceplatform.model.Solution;
import freelanceplatform.model.User;
import freelanceplatform.model.security.UserDetails;
import freelanceplatform.services.SolutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/rest/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;
    private final Mapper mapper;

    /**
     * Saves a new solution.
     *
     * @param solution Solution object to be saved.
     * @return ResponseEntity indicating success and URI of the newly created resource.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> save(@RequestBody SolutionCreation solution) {
        Solution savedSolution = solutionService.save(mapper.toSolution(solution));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedSolution.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Retrieves a solution by its ID.
     *
     * @param id ID of the solution to retrieve.
     * @return ResponseEntity containing the retrieved Solution object if found, or 404 if not found.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SolutionReadUpdate> getById(@PathVariable Integer id) {
        SolutionReadUpdate solutionReadUpdate = mapper.toSolutionReadUpdate(solutionService.getById(id));
        return ResponseEntity.ok(solutionReadUpdate);
    }

    /**
     * Updates details of an existing solution.
     *
     * @param id              ID of the solution to update.
     * @param updatedSolution Updated details of the solution.
     * @return ResponseEntity indicating success or failure of the update operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody SolutionReadUpdate updatedSolution, Authentication auth) {
        Solution solution = mapper.toSolution(updatedSolution, id);
        if (!hasAccess(solution, auth))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        solutionService.update(solution);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a solution.
     *
     * @param id ID of the solution to delete.
     * @return ResponseEntity indicating success or failure of the delete operation.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Authentication auth) {
        Solution solution = solutionService.getById(id);
        if (!hasAccess(solution, auth)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        solutionService.delete(solution);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if the authenticated user has access to the given solution.
     *
     * <p>This method verifies if the user associated with the given authentication
     * details is the same as the freelancer assigned to the task related to the provided solution.
     *
     * @param solution the solution for which access is being checked
     * @param auth     the authentication object containing the user details
     * @return true if the authenticated user is the freelancer assigned to the task, false otherwise
     */
    private boolean hasAccess(Solution solution, Authentication auth) {
        final User user = ((UserDetails) auth.getPrincipal()).getUser();
        return solution.getTask().getFreelancer().getId().equals(user.getId());
    }
}
