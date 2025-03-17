package elchinasgarov.plantly_backend.dto;

import elchinasgarov.plantly_backend.model.ReminderType;

import java.time.LocalDateTime;

public record ReminderDto(
        Long id,
        Long plantId,
        String plantName,
        ReminderType reminderType,
        int repeatEvery,
        String repeatUnit,
        LocalDateTime reminderTime
) {
}
