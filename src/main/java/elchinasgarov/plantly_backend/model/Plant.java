package elchinasgarov.plantly_backend.model;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class Plant {
    @Id
    private Long id;

    private String commonName;

    @ElementCollection
    private List<String> scientificNames;

    @OneToOne(cascade = CascadeType.ALL)
    private DefaultImage image;

    public Plant() {
    }

    public Plant(Long id, String commonName, List<String> scientificNames, DefaultImage image) {
        this.id = id;
        this.commonName = commonName;
        this.scientificNames = scientificNames;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public List<String> getScientificNames() {
        return scientificNames;
    }

    public void setScientificNames(List<String> scientificNames) {
        this.scientificNames = scientificNames;
    }

    public DefaultImage getImage() {
        return image;
    }

    public void setImage(DefaultImage image) {
        this.image = image;
    }
}
