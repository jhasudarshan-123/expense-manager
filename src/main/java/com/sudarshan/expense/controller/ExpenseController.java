package com.sudarshan.expense.controller;

import com.sudarshan.expense.entity.Expense;
import com.sudarshan.expense.entity.User;
import com.sudarshan.expense.security.JwtUtil;
import com.sudarshan.expense.service.ExpenseService;
import com.sudarshan.expense.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ExpenseController(ExpenseService expenseService,
                             UserService userService,
                             JwtUtil jwtUtil) {
        this.expenseService = expenseService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // ✅ Create Expense (User auto attached from JWT)
    @PostMapping
    public Expense createExpense(
            @RequestBody Expense expense,
            @RequestHeader("Authorization") String authHeader) {

        // Remove "Bearer "
        String token = authHeader.substring(7);

        // Extract email from JWT
        String email = jwtUtil.extractEmail(token);

        // Fetch user from database
        User user = userService.findByEmail(email);

        // Attach user to expense
        expense.setUser(user);

        // Save expense
        return expenseService.saveExpense(expense);
    }

    // ✅ Get All Expenses
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }
}
