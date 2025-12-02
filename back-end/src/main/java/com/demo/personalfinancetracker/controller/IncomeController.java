package com.demo.personalfinancetracker.controller;

import com.demo.personalfinancetracker.model.Income;
import com.demo.personalfinancetracker.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "*")
public class IncomeController {

    @Autowired
    private IncomeRepository incomeRepository;

    @GetMapping
    public List<Income> getAllIncome() {
        return incomeRepository.findAll();
    }

    @PostMapping
    public Income addIncome(@RequestBody Income income) {
        if (income.getDate() == null) {
            income.setDate(LocalDate.now());
        }
        return incomeRepository.save(income);
    }

    @GetMapping("/summary")
    public Map<String, Double> getMonthlyIncomeSummary(
            @RequestParam int year,
            @RequestParam int month
    ) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Income> incomes = incomeRepository.findByDateBetween(start, end);

        double totalIncome = incomes.stream()
                .mapToDouble(i -> i.getAmount() != null ? i.getAmount() : 0.0)
                .sum();

        Map<String, Double> result = new HashMap<>();
        result.put("totalIncome", totalIncome);
        return result;
    }
}
