package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.ReminderDto;
import elchinasgarov.plantly_backend.model.ReminderType;
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
    public ResponseEntity<?> createReminder(@RequestBody ReminderDto reminderDto) {
        System.out.println("Received reminderDto: " + reminderDto);
        try {
            ReminderDto created = reminderService.createReminder(reminderDto);
            System.out.println("Saved and returning: " + created);
            return ResponseEntity.ok(created);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ReminderDto> updateReminder(@PathVariable long id, @RequestBody ReminderDto reminderDto) {
        return ResponseEntity.ok(reminderService.updateReminder(id, reminderDto));
    }

    @GetMapping
    public ResponseEntity<List<ReminderDto>> getAllReminders() {
        return ResponseEntity.ok(reminderService.getAllReminders());
    }

    @GetMapping("/{id}/{reminderType}")
    public ResponseEntity<ReminderDto> getReminderByIdAndType(
            @PathVariable Long id,
            @PathVariable ReminderType reminderType
    ) {
        ReminderDto reminder = reminderService.getReminderById(id, reminderType);
        return ResponseEntity.ok(reminder);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReminder(@PathVariable long id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }


}
