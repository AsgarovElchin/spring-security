package elchinasgarov.plantly_backend.service;

import elchinasgarov.plantly_backend.dto.ReminderDto;
import elchinasgarov.plantly_backend.mapper.ReminderMapper;
import elchinasgarov.plantly_backend.model.Reminder;
import elchinasgarov.plantly_backend.model.ReminderType;
import elchinasgarov.plantly_backend.repository.ReminderRepository;
import elchinasgarov.plantly_backend.util.ReminderDateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public ReminderDto createReminder(ReminderDto reminderDto) {
        boolean exists = reminderRepository.existsByPlantIdAndReminderType(
                reminderDto.plantId(), reminderDto.reminderType()
        );

        if (exists) {
            throw new IllegalStateException("A reminder for this plant and type already exists");
        }

        LocalTime userChosenTime = reminderDto.reminderTime().toLocalTime();

        LocalDateTime calculatedNextReminderDateTime = ReminderDateUtil.calculateNextReminderDateTime(
                reminderDto.previousData(),
                reminderDto.repeatEvery(),
                reminderDto.repeatUnit(),
                userChosenTime
        );

        Reminder reminder = new Reminder(
                null,
                reminderDto.plantId(),
                reminderDto.plantName(),
                reminderDto.reminderType(),
                reminderDto.repeatEvery(),
                reminderDto.repeatUnit(),
                reminderDto.reminderTime(),
                calculatedNextReminderDateTime,
                reminderDto.previousData()
        );




        Reminder savedReminder = reminderRepository.save(reminder);
        return ReminderMapper.toDto(savedReminder);
    }


    public ReminderDto updateReminder(Long id, ReminderDto reminderDto){
        Reminder reminder = reminderRepository.findById(id).orElseThrow(()-> new RuntimeException("Reminder not found"));
        reminder.setReminderType(reminderDto.reminderType());
        reminder.setRepeatEvery(reminderDto.repeatEvery());
        reminder.setRepeatUnit(reminderDto.repeatUnit());
        reminder.setReminderTime(reminderDto.reminderTime());

        LocalDateTime recalculatedNextReminder = ReminderDateUtil.calculateNextReminderDateTime(
                reminderDto.previousData(),
                reminderDto.repeatEvery(),
                reminderDto.repeatUnit(),
                reminderDto.reminderTime().toLocalTime()
        );
        reminder.setNextReminderDateTime(recalculatedNextReminder);

        return ReminderMapper.toDto(reminderRepository.save(reminder));

    }

    public List<ReminderDto> getAllReminders(){
        return reminderRepository.findAll()
                .stream()
                .map(ReminderMapper::toDto)
                .collect(Collectors.toList());
    }

    public ReminderDto getReminderById(Long id, ReminderType reminderType){
        Reminder reminder = reminderRepository.findByIdAndReminderType(id,reminderType)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        return  ReminderMapper.toDto(reminder);
    }

    public void deleteReminder(Long id){
        if (!reminderRepository.existsById(id)) {
            throw new RuntimeException("Reminder not found");
        }
        reminderRepository.deleteById(id);
    }




}
