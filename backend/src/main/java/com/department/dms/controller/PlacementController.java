package com.department.dms.controller;

import com.department.dms.model.Placement;
import com.department.dms.service.PlacementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/placements")
@CrossOrigin
public class PlacementController {

    private final PlacementService service;

    public PlacementController(PlacementService service) {
        this.service = service;
    }

    @PostMapping
    public Placement addPlacement(@RequestBody Placement placement) {
        return service.save(placement);
    }

    @GetMapping
    public List<Placement> getPlacements() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public void deletePlacement(@PathVariable String id) {
        service.delete(id);
    }
}