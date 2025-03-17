package elchinasgarov.plantly_backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long plantId;

    private String plantName;

    @Enumerated(EnumType.STRING)
    private ReminderType reminderType;

    private int repeatEvery;

    private String repeatUnit;

    private LocalDateTime reminderTime;

    public Reminder() {
    }

    public Reminder(Long id, Long plantId, String plantName, ReminderType reminderType, int repeatEvery, String repeatUnit, LocalDateTime reminderTime) {
        this.id = id;
        this.plantId = plantId;
        this.plantName = plantName;
        this.reminderType = reminderType;
        this.repeatEvery = repeatEvery;
        this.repeatUnit = repeatUnit;
        this.reminderTime = reminderTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlantId() {
        return plantId;
    }

    public void setPlantId(Long plantId) {
        this.plantId = plantId;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public ReminderType getReminderType() {
        return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
        this.reminderType = reminderType;
    }

    public int getRepeatEvery() {
        return repeatEvery;
    }

    public void setRepeatEvery(int repeatEvery) {
        this.repeatEvery = repeatEvery;
    }

    public String getRepeatUnit() {
        return repeatUnit;
    }

    public void setRepeatUnit(String repeatUnit) {
        this.repeatUnit = repeatUnit;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
}
