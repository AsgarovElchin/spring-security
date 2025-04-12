package elchinasgarov.plantly_backend.service;

import elchinasgarov.plantly_backend.dto.PlantDto;
import elchinasgarov.plantly_backend.mapper.PlantMapper;
import elchinasgarov.plantly_backend.model.MyUser;
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

    public List<PlantDto> getAllPlantsForUser(Integer userId){
        return plantRepository.findAllByUserId(userId)
                .stream()
                .map(PlantMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PlantDto savePlant(PlantDto plantDto, MyUser user){
        Plant plant = PlantMapper.toEntity(plantDto);
        plant.setUser(user);
        return PlantMapper.toDTO(plantRepository.save(plant));
    }


    public void deletePlantById(Long id){
        if (!plantRepository.existsById(id)) {
            throw new RuntimeException("Plant not found with ID: " + id);
        }
        plantRepository.deleteById(id);
    }
}

