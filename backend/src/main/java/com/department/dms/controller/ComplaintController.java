package com.department.dms.controller;

import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.department.dms.model.Complaint;
import com.department.dms.service.ComplaintService;

@RestController
@CrossOrigin
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    @PostMapping
    public Complaint add(@RequestBody Complaint complaint) {
        return service.save(normalizeComplaint(complaint));
    }

    @GetMapping
    public List<Complaint> getAll() {
        return service.getAll();
    }

    private Complaint normalizeComplaint(Complaint complaint) {
        if (complaint == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complaint payload is required");
        }

        String role = trimToNull(complaint.getRole());
        String category = trimToNull(complaint.getCategory());
        String details = trimToNull(complaint.getDetails());

        if (role == null || category == null || details == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role, category and details are required");
        }

        complaint.setName(defaultIfBlank(complaint.getName(), "Anonymous"));
        complaint.setRole(role);
        complaint.setContact(trimToNull(complaint.getContact()));
        complaint.setCategory(category);
        complaint.setDetails(details);
        complaint.setSubmittedAt(Instant.now());
        return complaint;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String defaultIfBlank(String value, String fallback) {
        String trimmed = trimToNull(value);
        return trimmed == null ? fallback : trimmed;
    }
}
