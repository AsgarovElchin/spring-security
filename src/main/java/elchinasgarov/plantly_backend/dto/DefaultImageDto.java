package elchinasgarov.plantly_backend.dto;

public record DefaultImageDto(
        String originalUrl,
        String regularUrl,
        String mediumUrl,
        String smallUrl,
        String thumbnailUrl
) {
}
