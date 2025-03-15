package elchinasgarov.plantly_backend.mapper;

import elchinasgarov.plantly_backend.dto.DefaultImageDto;
import elchinasgarov.plantly_backend.dto.PlantDto;
import elchinasgarov.plantly_backend.model.DefaultImage;
import elchinasgarov.plantly_backend.model.Plant;

public class PlantMapper {

    public static Plant toEntity(PlantDto dto) {
        return new Plant(dto.id(), dto.commonName(), dto.scientificNames(), toEntity(dto.defaultImage()));
    }

    public static PlantDto toDTO(Plant plant) {
        return new PlantDto(plant.getId(), plant.getCommonName(), plant.getScientificNames(), toDTO(plant.getImage()));
    }

    private static DefaultImage toEntity(DefaultImageDto dto) {
        if (dto == null) return null;
        return new DefaultImage(null, dto.originalUrl(), dto.regularUrl(), dto.mediumUrl(), dto.smallUrl(), dto.thumbnailUrl());
    }

    private static DefaultImageDto toDTO(DefaultImage entity) {
        if (entity == null) return null;
        return new DefaultImageDto(entity.getOriginalUrl(), entity.getRegularUrl(), entity.getMediumUrl(), entity.getSmallUrl(), entity.getThumbnailUrl());
    }
}
