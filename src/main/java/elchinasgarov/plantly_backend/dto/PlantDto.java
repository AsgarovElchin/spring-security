package elchinasgarov.plantly_backend.dto;

import java.util.List;

public record PlantDto(
        Long id,
        String commonName,
        List<String> scientificNames,
        DefaultImageDto defaultImage
) {
}