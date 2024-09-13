package totom.ru.vacationcalculator.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class VacationService {

    /*
    Логика расчета отпускных:
    Предполагается что averageSalary это средняя зп за 12 месяцев, то есть общий доход за год / 12 месяцев / 29,3 (возможны корректировки)
    salaryPerDay * numberOfDays получаем vacationPay
    Так же
    */
    public double calculateVacationPay(double averageSalary, int numberOfDays, LocalDate startDate, LocalDate endDate) {
        int workingDays = (numberOfDays > 0 ? numberOfDays : getNumberOfWorkingDaysByDates(startDate, endDate));
        return averageSalary * workingDays;
    }


    // Метод для получения праздников
    // В реальном приложении можно использовать стороннюю библиотеку или API для получения праздников
    private static Set<LocalDate> getHolidays(LocalDate startDate) {
        Set<LocalDate> holidays = new HashSet<>(); //Список праздников
        holidays.add(LocalDate.of(startDate.getYear(), 1, 1)); // Новый год
        holidays.add(LocalDate.of(startDate.getYear(), 1, 2));
        holidays.add(LocalDate.of(startDate.getYear(), 1, 3));
        holidays.add(LocalDate.of(startDate.getYear(), 1, 4));
        holidays.add(LocalDate.of(startDate.getYear(), 1, 5));
        holidays.add(LocalDate.of(startDate.getYear(), 1, 6));
        holidays.add(LocalDate.of(startDate.getYear(), 1, 7));
        holidays.add(LocalDate.of(startDate.getYear(), 1, 8));
        holidays.add(LocalDate.of(startDate.getYear(), 2, 23)); // День защитника отечества
        holidays.add(LocalDate.of(startDate.getYear(), 3, 8)); // Международный женский день
        holidays.add(LocalDate.of(startDate.getYear(), 5, 1)); // Праздник Весны и Труда
        holidays.add(LocalDate.of(startDate.getYear(), 5, 9)); // День Победы
        holidays.add(LocalDate.of(startDate.getYear(), 6, 12)); // День России
        holidays.add(LocalDate.of(startDate.getYear(), 11, 4)); // День народного единства
        // Возможны корректировки
        return holidays;
    }

    //Метод, чтобы посчитать количество рабочих дней в даты отпуска.
    // Создаем стрим для дат между началом и концом отпуска и фильтруем нерабочие дни
    private static int getNumberOfWorkingDaysByDates(LocalDate startDate, LocalDate endDate) {
        return (int) Stream.
                iterate(startDate, date -> !date.isAfter(endDate), date -> date.plusDays(1))
                .filter(date -> isWorkingDay(date, getHolidays(startDate)))
                .count();
    }

    //Служебный метод для проверки является ли день рабочим, проверяет на выходные и праздники
    private static boolean isWorkingDay(LocalDate date, Set<LocalDate> holidays){
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || holidays.contains(date));
    }
}
