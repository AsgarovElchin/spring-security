package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.ReminderDto;
import elchinasgarov.plantly_backend.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<ReminderDto> createReminder(@RequestBody ReminderDto reminderDto) {
        return ResponseEntity.ok(reminderService.createReminder(reminderDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderDto> updateReminder(@PathVariable long id, @RequestBody ReminderDto reminderDto) {
        return ResponseEntity.ok(reminderService.updateReminder(id, reminderDto));
    }

    @GetMapping
    public ResponseEntity<List<ReminderDto>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderDto> getReminderById(@PathVariable long id) {
        return ResponseEntity.ok(reminderService.getReminderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }


}
