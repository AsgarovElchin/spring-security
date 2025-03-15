package elchinasgarov.plantly_backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class DefaultImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String originalUrl;

    private String regularUrl;

    private String mediumUrl;

    private String smallUrl;

    private String thumbnailUrl;

    public DefaultImage() {
    }

    public DefaultImage(Long id, String originalUrl, String regularUrl, String mediumUrl, String smallUrl, String thumbnailUrl) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.regularUrl = regularUrl;
        this.mediumUrl = mediumUrl;
        this.smallUrl = smallUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getRegularUrl() {
        return regularUrl;
    }

    public void setRegularUrl(String regularUrl) {
        this.regularUrl = regularUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}

