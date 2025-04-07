package elchinasgarov.plantly_backend.controller;

import elchinasgarov.plantly_backend.dto.ApiErrorResponse;
import elchinasgarov.plantly_backend.dto.ApiResponse;
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
        try {
            ReminderDto created = reminderService.createReminder(reminderDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Reminder created", created));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(new ApiErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiErrorResponse("Failed to create reminder"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReminder(@PathVariable long id, @RequestBody ReminderDto reminderDto) {
        ReminderDto updated = reminderService.updateReminder(id, reminderDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Reminder updated", updated));
    }

    @GetMapping
    public ResponseEntity<?> getAllReminders() {
        List<ReminderDto> reminders = reminderService.getAllReminders();
        return ResponseEntity.ok(new ApiResponse<>(true, "All reminders fetched", reminders));
    }

    @GetMapping("/{id}/{reminderType}")
    public ResponseEntity<?> getReminderByIdAndType(
            @PathVariable Long id,
            @PathVariable ReminderType reminderType
    ) {
        try {
            ReminderDto reminder = reminderService.getReminderById(id, reminderType);
            return ResponseEntity.ok(new ApiResponse<>(true, "Reminder fetched", reminder));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable long id) {
        try {
            reminderService.deleteReminder(id);
            return ResponseEntity.ok(new ApiResponse<>(true, "Reminder deleted", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiErrorResponse(e.getMessage()));
        }
    }
}
