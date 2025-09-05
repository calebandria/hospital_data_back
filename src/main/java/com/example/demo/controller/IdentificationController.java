package com.example.demo.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Role;
import com.example.demo.model.Identification;
import com.example.demo.payload.IdentificationRegistration;
import com.example.demo.payload.MessageResponse;
import com.example.demo.service.IdentificationService;
import com.example.demo.payload.IdenficationCodeRequest;
import com.example.demo.payload.IdentificationCodeResponse;

import jakarta.validation.Valid;

@Controller
@RestController
@RequestMapping("/api/identification")
public class IdentificationController {

    @Autowired
    private final IdentificationService identificationService;

    public IdentificationController(IdentificationService identificationService) {
        this.identificationService = identificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerIdentification(
            @Valid @RequestBody IdentificationRegistration identificationRegistration) {
        try {
            identificationService.registerIdentification(identificationRegistration.getIdentification(), Role.valueOf(identificationRegistration.getRole()));
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponse("Identifiant enregistré avec succès"));
        }

        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/free")
    public ResponseEntity<List<Identification>> getAllFreeIdentifiant() {
        List<Identification> identifications = identificationService.findAllFreedIdenfications();

        if (identifications.isEmpty()) {
            // Returns 204 No Content if the list is empty
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            // Returns 200 OK with the list of identifications
            return new ResponseEntity<>(identifications, HttpStatus.OK);
        }
    }

    @GetMapping("/validate-code/{code}")
    public ResponseEntity<?> validateCode(@Valid @PathVariable long code){
        String role = null;
        try {
             Identification foundIdentification = identificationService.findSingleFreeIdentification(code);
             if (foundIdentification != null){
                role = foundIdentification.getRole().toString();
                return  ResponseEntity.ok(new IdentificationCodeResponse(role,code));
            }
            else {
                return  ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new MessageResponse("User already assigned to it!"));
            }
        }
        catch(NoSuchElementException err){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new MessageResponse("No existing identification"));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIdentification(@PathVariable("id") String id) {
        try {
            int idInteger = Integer.parseInt(id);
            
            // Call the service method that now returns a boolean
            boolean isDeleted = identificationService.deleteIdentification(idInteger);

            if (isDeleted) {
                // Returns 200 OK if the identification was successfully deleted
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                // Returns 404 Not Found if the ID did not exist
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            // Returns 400 Bad Request if the ID is not a valid number
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
