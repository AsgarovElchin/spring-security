package elchinasgarov.plantly_backend.service;

import elchinasgarov.plantly_backend.dto.PlantDto;
import elchinasgarov.plantly_backend.mapper.PlantMapper;
import elchinasgarov.plantly_backend.model.Plant;
import elchinasgarov.plantly_backend.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;


    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public PlantDto savePlant(PlantDto plantDto){
        Plant plant = PlantMapper.toEntity(plantDto);
        Plant savedPlant = plantRepository.save(plant);
        return PlantMapper.toDTO(savedPlant);
    }

    public List<PlantDto> getAllPlants(){
        return plantRepository.findAll()
                .stream()
                .map(plant -> PlantMapper.toDTO(plant))
                .collect(Collectors.toList());
    }

    public void deletePlantById(Long id){
        if (!plantRepository.existsById(id)) {
            throw new RuntimeException("Plant not found with ID: " + id);
        }
        plantRepository.deleteById(id);
    }
}

