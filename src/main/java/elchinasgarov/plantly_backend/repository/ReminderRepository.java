package elchinasgarov.plantly_backend.repository;

import elchinasgarov.plantly_backend.model.Reminder;
import elchinasgarov.plantly_backend.model.ReminderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    boolean existsByPlantIdAndReminderType(Long plantId, ReminderType reminderType);

    Optional<Reminder> findByIdAndReminderType(Long id, ReminderType reminderType);

    List<Reminder> findAllByUserId(Integer userId);


}
