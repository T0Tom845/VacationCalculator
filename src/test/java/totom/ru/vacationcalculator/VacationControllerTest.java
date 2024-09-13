package totom.ru.vacationcalculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import totom.ru.vacationcalculator.api.controller.VacationController;
import totom.ru.vacationcalculator.service.VacationService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты VacationController")
public class VacationControllerTest {

    @InjectMocks
    private VacationController vacationController;

    @Mock
    private VacationService vacationService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(vacationController).build();
    }

    @Test
    @DisplayName("calculateVacationPay принял кол-во дней, вернет сумму отпускных")
    void calculateVacationPay_RequestIsValidNumberOfDaysSent_ResponseIsOk() throws Exception {
        when(vacationService.calculateVacationPay(100,10, null, null)).thenReturn(1000.0);
        mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "100")
                        .param("numberOfDays", "10")
                        )
                .andExpect(status().isOk())
                .andExpect(content().string("1000.0"));
    }

    @Test
    @DisplayName("calculateVacationPay принял даты, вернет сумму отпускных")
    void calculateVacationPay_RequestIsValidDatesSent_ResponseIsOk() throws Exception {
        when(vacationService.calculateVacationPay(100,0,
                LocalDate.parse("01.09.2024", DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                LocalDate.parse("16.09.2024", DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        )).thenReturn(1100.0);
        mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                        .param("averageSalary", "100")
                        .param("startDate", "01.09.2024")
                        .param("endDate", "16.09.2024")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1100.0"));
    }

    @Test
    @DisplayName("calculateVacationPay запрос без параметров возвращает ошибку")
    void calculateVacationPay_RequestWithoutParameters_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/calculate"))
                .andExpect(status().isBadRequest());
    }
}
