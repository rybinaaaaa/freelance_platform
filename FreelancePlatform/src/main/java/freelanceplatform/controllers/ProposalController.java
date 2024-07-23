package freelanceplatform.controllers;

import freelanceplatform.dto.Mapper;
import freelanceplatform.dto.creation.ProposalCreation;
import freelanceplatform.dto.readUpdate.ProposalReadUpdate;
import freelanceplatform.model.Proposal;
import freelanceplatform.model.User;
import freelanceplatform.model.security.UserDetails;
import freelanceplatform.services.ProposalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.FORBIDDEN;

/**
 * Controller for managing proposals.
 */
@Slf4j
@RestController
@RequestMapping("/rest/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;
    private final Mapper mapper;

    private final static ResponseEntity<Void> FORBIDDEN1 = new ResponseEntity<>(FORBIDDEN);
    private final static ResponseEntity<Void> BAD_REQUEST = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    /**
     * Finds a proposal by its ID.
     *
     * @param id the ID of the proposal
     * @return the proposal DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProposalReadUpdate> findById(@PathVariable Integer id) {
        return proposalService.findById(id)
                .map(pr -> ResponseEntity.ok(mapper.toProposalReadUpdate(pr)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Finds all proposals.
     *
     * @return a list of all proposal DTOs
     */
    @GetMapping()
    public ResponseEntity<List<ProposalReadUpdate>> findAll() {
        return ResponseEntity.ok(proposalService.findAll().stream()
                .map(mapper::toProposalReadUpdate).toList());
    }

    /**
     * Updates an existing proposal.
     *
     * @param id          the ID of the proposal to update
     * @param proposalReadUpdate the proposal DTO with updated information
     * @param auth        the authentication object
     * @return a response entity indicating the outcome
     */
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody ProposalReadUpdate proposalReadUpdate, Authentication auth) {
        Objects.requireNonNull(proposalReadUpdate);

        if (!proposalReadUpdate.getId().equals(id)) return BAD_REQUEST;
        if (!hasUserAccess(proposalReadUpdate, auth)) return FORBIDDEN1;

        Proposal newPr = mapper.toProposal(proposalReadUpdate);

        proposalService.update(newPr);
        return ResponseEntity.noContent().build();
    }

    /**
     * Saves a new proposal.
     *
     * @param proposalCreation the proposal DTO to save
     * @param auth                the authentication object
     * @return a response entity indicating the outcome
     */
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_ADMIN'})")
    @PostMapping()
    public ResponseEntity<Void> save(@RequestBody ProposalCreation proposalCreation, Authentication auth) {
        Objects.requireNonNull(proposalCreation);

        if (!hasUserAccess(proposalCreation, auth)) return FORBIDDEN1;

        Proposal newPr = mapper.toProposal(proposalCreation);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(proposalService.save(newPr).getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Deletes a proposal by its ID.
     *
     * @param id the ID of the proposal to delete
     * @return a response entity indicating the outcome
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        return proposalService.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Checks if the authenticated user has access to the proposal.
     *
     * @param proposalReadUpdate the proposal DTO
     * @param auth        the authentication object
     * @return true if the user has access, false otherwise
     */
    private static Boolean hasUserAccess(ProposalReadUpdate proposalReadUpdate, Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        return user.isAdmin() || user.getId().equals(proposalReadUpdate.getFreelancerId());
    }

    /**
     * Checks if the authenticated user has access to the proposal.
     *
     * @param proposalCreation the proposal DTO
     * @param auth                the authentication object
     * @return true if the user has access, false otherwise
     */
    private static Boolean hasUserAccess(ProposalCreation proposalCreation, Authentication auth) {
        User user = ((UserDetails) auth.getPrincipal()).getUser();
        return user.isAdmin() || user.getId().equals(proposalCreation.getFreelancerId());
    }
}
