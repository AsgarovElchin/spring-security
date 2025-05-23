package elchinasgarov.plantly_backend.repository;

import elchinasgarov.plantly_backend.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    List<Plant> findAllByUserId(Integer userId);


}
