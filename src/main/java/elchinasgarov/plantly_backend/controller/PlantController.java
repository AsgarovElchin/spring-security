package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.ApiErrorResponse;
import elchinasgarov.plantly_backend.dto.ApiResponse;
import elchinasgarov.plantly_backend.dto.PlantDto;
import elchinasgarov.plantly_backend.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping
    public ResponseEntity<?> addPlant(@RequestBody PlantDto plantDto) {
        try {
            PlantDto savedPlant = plantService.savePlant(plantDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Plant created successfully", savedPlant));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorResponse("Failed to create plant"));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPlants() {
        List<PlantDto> plants = plantService.getAllPlants();
        return ResponseEntity.ok(new ApiResponse<>(true, "Plants fetched", plants));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlant(@PathVariable Long id) {
        try {
            plantService.deletePlantById(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Plant deleted", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorResponse(e.getMessage()));
        }
    }
}