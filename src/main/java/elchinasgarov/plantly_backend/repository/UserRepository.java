package elchinasgarov.plantly_backend.repository;

import elchinasgarov.plantly_backend.model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<MyUser, Integer> {
}
