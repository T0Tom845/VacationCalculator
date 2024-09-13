package totom.ru.vacationcalculator.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import totom.ru.vacationcalculator.service.VacationService;

import java.time.LocalDate;

@RestController
public class VacationController {

    private VacationService vacationService;

    VacationController(VacationService vacationService){
        this.vacationService = vacationService;
    }


    /*  Получение отпускных по количеству дней
        numberOfDays, startDate и endDate не обязательные тк если дали numberOfDays, то
        startDate и endDate не дадут и наоборот. Если не дали все три бросить ошибку
     */
    @GetMapping("/calculate")
    public ResponseEntity<String> calculateVacationPay(
            @RequestParam double averageSalary,
            @RequestParam(required = false) Integer numberOfDays,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "M/d/yy", "dd.MM.yyyy" }) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = { "M/d/yy", "dd.MM.yyyy" }) LocalDate endDate)
    {
        if ( averageSalary <= 0 )
            return ResponseEntity.badRequest().body("Bad request: Negative average salary value"); //Неверный запрос, негативная зарплата
        if (numberOfDays != null){
            if (numberOfDays < 0) return ResponseEntity.badRequest().body("Bad request: Negative Number of days value"); //Неверный запрос, негативная количество дней
        }
        if (numberOfDays == null && ((startDate == null || endDate == null)))
            return ResponseEntity.badRequest().body("Bad request: Required arguments is missing"); //Неверный запрос, отсутствуют необходимые параметры
        if (startDate != null && endDate != null){
            if ((startDate.isAfter(endDate))) //Неверный запрос, дата начала позже даты конца
                return ResponseEntity.badRequest().body("Bad request: Start date is after End date");
        }

        double vacationPay = vacationService.calculateVacationPay(averageSalary, numberOfDays != null ? numberOfDays : 0, startDate, endDate);
        return ResponseEntity.ok(Double.toString(vacationPay));
    }

}