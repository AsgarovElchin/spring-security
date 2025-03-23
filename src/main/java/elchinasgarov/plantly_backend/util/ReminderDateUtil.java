package elchinasgarov.plantly_backend.util;

import elchinasgarov.plantly_backend.model.PreviousData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class ReminderDateUtil {

    public static LocalDate getLastActionDate(PreviousData previousData) {
        LocalDate today = LocalDate.now();
        return switch (previousData) {
            case TODAY -> today;
            case YESTERDAY -> today.minusDays(1);
            case ONE_WEEK_AGO -> today.minusWeeks(1);
            case TWO_WEEKS_AGO -> today.minusWeeks(2);
            case THREE_WEEKS_AGO -> today.minusWeeks(3);
            case A_MONTH_AGO -> today.minusMonths(1);
            case MORE_THAN_A_MONTH -> today.minusMonths(2);
            case DONT_REMEMBER -> today.minusWeeks(1); // default fallback
        };
    }


    public static LocalDateTime calculateNextReminderDateTime(
            PreviousData previousData,
            int repeatEvery,
            String repeatUnit,
            LocalTime timeOfDay
    ) {
        LocalDate today = LocalDate.now();
        LocalDate nextDate = getLastActionDate(previousData);

        while (!nextDate.isAfter(today)) {
            nextDate = switch (repeatUnit.toLowerCase()) {
                case "days" -> nextDate.plusDays(repeatEvery);
                case "weeks" -> nextDate.plusWeeks(repeatEvery);
                case "months" -> nextDate.plusMonths(repeatEvery);
                default -> throw new IllegalArgumentException("Invalid repeat unit: " + repeatUnit);
            };
        }

        return LocalDateTime.of(nextDate, timeOfDay);
    }
}
