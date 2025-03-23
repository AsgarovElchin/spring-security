package elchinasgarov.plantly_backend.mapper;

import elchinasgarov.plantly_backend.dto.ReminderDto;
import elchinasgarov.plantly_backend.model.Reminder;

public class ReminderMapper {

    public static Reminder toEntity(ReminderDto reminderDto){
        return new Reminder(
                reminderDto.id(),
                reminderDto.plantId(),
                reminderDto.plantName(),
                reminderDto.reminderType(),
                reminderDto.repeatEvery(),
                reminderDto.repeatUnit(),
                reminderDto.reminderTime(),
                reminderDto.nextReminderDateTime(),
                reminderDto.previousData()
        );
    }

    public static ReminderDto toDto(Reminder reminder){
        return new ReminderDto(
                reminder.getId(),
                reminder.getPlantId(),
                reminder.getPlantName(),
                reminder.getReminderType(),
                reminder.getRepeatEvery(),
                reminder.getRepeatUnit(),
                reminder.getReminderTime(),
                reminder.getPreviousData(),
                reminder.getNextReminderDateTime()
        );
    }
}
