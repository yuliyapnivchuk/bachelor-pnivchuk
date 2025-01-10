package com.itstep.service;

import com.itstep.entity.Expense;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.exception.NonExistingSplitType;
import com.itstep.repository.ExpenseItemProjection;
import com.itstep.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class BalanceService {

    private ExpenseRepository expenseRepository;

    public Map<String, Map<String, Double>> calculateWhatUserIsOwed(String user) {
        List<ExpenseItemProjection> list = expenseRepository.findItems(user);

        Map<String, Map<String, Double>> userDebtsBalance = initialiseUserDebtsBalanceMap(list);
        Map<Integer, String> itemsAndItsSplitTypes = getItemsAndItsSplitTypes(list);

        for (Map.Entry<Integer, String> expenseItems : itemsAndItsSplitTypes.entrySet()) {

            List<ExpenseItemProjection> itemShareComponents = list
                    .stream()
                    .filter(i -> i.getItemId().equals(expenseItems.getKey()))
                    .toList();

            switch (SplitType.get(expenseItems.getValue())) {
                case EQUAL:
                    splitEqually(itemShareComponents, userDebtsBalance);
                    break;

                case SHARES:
                    splitByShares(itemShareComponents, userDebtsBalance);
                    break;

                case PERCENTAGE:
                    splitByPercentage(itemShareComponents, userDebtsBalance);
                    break;

                case MANUAL:
                    splitByManuallyEnteredAmounts(itemShareComponents, userDebtsBalance);
                    break;

                default:
                    throw new NonExistingSplitType("Non existing split type");
            }
        }

        splitFeeAndTax(list, userDebtsBalance);

        return userDebtsBalance;
    }

    private Map<String, Map<String, Double>> initialiseUserDebtsBalanceMap(List<ExpenseItemProjection> list) {
        Map<String, Map<String, Double>> userDebtsBalance = new HashMap<>();

        Set<String> users = list
                .stream()
                .map(ExpenseItemProjection::getUserName)
                .collect(Collectors.toSet());

        for (String user : users) {
            Map<String, Double> currencies = new HashMap<>();
            list.stream()
                    .filter(l -> l.getUserName().equals(user))
                    .forEach(l -> currencies.putIfAbsent(l.getCurrency(), 0.0));

            userDebtsBalance.put(user, currencies);
        }
        return userDebtsBalance;
    }

    private Map<Integer, String> getItemsAndItsSplitTypes(List<ExpenseItemProjection> list) {
        Map<Integer, String> itemsAndItsSplitTypes = new HashMap<>();
        list.forEach(line -> itemsAndItsSplitTypes.putIfAbsent(line.getItemId(), line.getSplitType()));
        return itemsAndItsSplitTypes;
    }

    private void splitEqually(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double userShare = tmp.getTotalPrice() / itemShareComponents.size();
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + userShare);
            userDebtsBalance.put(tmp.getUserName(), userBalance);
        }
    }

    private void splitByShares(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        Double shareSum = itemShareComponents
                .stream()
                .map(ExpenseItemProjection::getValue)
                .reduce(0.0, Double::sum);

        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double userShare = tmp.getTotalPrice() / shareSum * tmp.getValue();
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + userShare);
            userDebtsBalance.put(tmp.getUserName(), userBalance);
        }
    }

    private void splitByPercentage(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double percentageAmount = tmp.getTotalPrice() / 100 * tmp.getValue();
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + percentageAmount);
            userDebtsBalance.put(tmp.getUserName(), userBalance);
        }
    }

    private void splitByManuallyEnteredAmounts(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + tmp.getValue());
            userDebtsBalance.put(tmp.getUserName(), userBalance);
        }
    }

    @SneakyThrows
    private void splitFeeAndTax(List<ExpenseItemProjection> expenseItems, Map<String, Map<String, Double>> userDebtsBalance) {
        Set<Integer> expenseIds = expenseItems
                .stream()
                .map(ExpenseItemProjection::getExpenseId)
                .collect(Collectors.toSet());

        for (Integer expenseId : expenseIds) {
            Expense expense = expenseRepository.findById(expenseId)
                    .orElseThrow(() -> new ExpenseNotFound(EXPENSE_WITH_SUCH_ID_NOT_FOUND));

            double feeAndTax = expense.getTotalAmount() - expense.getSubtotalAmount();

            if (feeAndTax == 0) {
                continue;
            }

            Set<String> expenseUsers = expenseItems
                    .stream()
                    .filter(line -> line.getExpenseId().equals(expenseId))
                    .map(ExpenseItemProjection::getUserName)
                    .collect(Collectors.toSet());

            Double feeAndTaxUserFraction = feeAndTax / expenseUsers.size();

            for (String user : expenseUsers) {
                Map<String, Double> userBalance = userDebtsBalance.get(user);
                Double existingAmount = userBalance.get(expense.getCurrency());
                userBalance.put(expense.getCurrency(), existingAmount + feeAndTaxUserFraction);
            }
        }
    }
}