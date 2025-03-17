package elchinasgarov.plantly_backend.repository;

import elchinasgarov.plantly_backend.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {


}
