package com.itstep.service;

import com.itstep.entity.Expense;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.exception.NonExistingSplitType;
import com.itstep.repository.ExpenseItemProjection;
import com.itstep.repository.ExpenseRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class BalanceService {

    private ExpenseRepository expenseRepository;

    public Map<String, Map<String, Double>> calculateWhatUserIsOwed(String user) {
        List<ExpenseItemProjection> list = expenseRepository.findUserIsOwedItems(user);

        Map<String, Map<String, Double>> userDebtsBalance = initUserIsOwedBalanceMap(list);
        Map<Integer, String> itemsAndItsSplitTypes = getItemsAndItsSplitTypes(list);

        for (Map.Entry<Integer, String> expenseItems : itemsAndItsSplitTypes.entrySet()) {

            List<ExpenseItemProjection> itemShareComponents = list
                    .stream()
                    .filter(i -> i.getItemId().equals(expenseItems.getKey()))
                    .toList();

            switch (SplitType.get(expenseItems.getValue())) {
                case EQUAL:
                    userIsOwedBalanceSplitEqually(itemShareComponents, userDebtsBalance);
                    break;

                case SHARES:
                    userIsOwedBalanceSplitByShares(itemShareComponents, userDebtsBalance);
                    break;

                case PERCENTAGE:
                    userIsOwedBalanceSplitByPercentage(itemShareComponents, userDebtsBalance);
                    break;

                case MANUAL:
                    userIsOwedBalanceSplitByManuallyEnteredAmounts(itemShareComponents, userDebtsBalance);
                    break;

                default:
                    throw new NonExistingSplitType("Non existing split type");
            }
        }

        userIsOwedBalanceSplitFeeAndTax(list, userDebtsBalance);

        return userDebtsBalance;
    }

    public Map<String, Map<String, Double>> calculateWhatUserOwes(String user) {
        List<ExpenseItemProjection> list = expenseRepository.findUserOweItems(user);

        Map<String, Map<String, Double>> userOweBalances = initUserOwesBalanceMap(list);
        Map<Integer, String> itemsAndItsSplitTypes = getItemsAndItsSplitTypes(list);

        for (Map.Entry<Integer, String> expenseItems : itemsAndItsSplitTypes.entrySet()) {

            List<ExpenseItemProjection> itemShareComponents = list
                    .stream()
                    .filter(i -> i.getItemId().equals(expenseItems.getKey()))
                    .toList();

            Optional<ExpenseItemProjection> userOweItem = itemShareComponents
                    .stream()
                    .filter(i -> i.getUserName().equals(user))
                    .findFirst();

            if (userOweItem.isEmpty()) {
                continue;
            }

            switch (SplitType.get(expenseItems.getValue())) {
                case EQUAL:
                    userOwesBalanceSplitEqually(itemShareComponents.size(), userOweItem.get(), userOweBalances);
                    break;

                case SHARES:
                    userOwesBalanceSplitByShares(itemShareComponents, userOweItem.get(), userOweBalances);
                    break;

                case PERCENTAGE:
                    userOwesBalanceSplitByPercentage(userOweItem.get(), userOweBalances);
                    break;

                case MANUAL:
                    userOwesBalanceSplitByManuallyEnteredAmounts(userOweItem.get(), userOweBalances);
                    break;

                default:
                    throw new NonExistingSplitType("Non existing split type");
            }
        }

        userOwesBalanceSplitFeeAndTax(list, userOweBalances);

        return userOweBalances;
    }

    private Map<String, Map<String, Double>> initUserIsOwedBalanceMap(List<ExpenseItemProjection> list) {
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

    private Map<String, Map<String, Double>> initUserOwesBalanceMap(List<ExpenseItemProjection> list) {
        Map<String, Map<String, Double>> userOweBalances = new HashMap<>();

        Set<String> payers = list
                .stream()
                .map(ExpenseItemProjection::getPayer)
                .collect(Collectors.toSet());

        for (String payer : payers) {
            Map<String, Double> currencies = new HashMap<>();
            list.stream()
                    .filter(l -> l.getPayer().equals(payer))
                    .forEach(l -> currencies.putIfAbsent(l.getCurrency(), 0.0));

            userOweBalances.put(payer, currencies);
        }

        return userOweBalances;
    }

    private Map<Integer, String> getItemsAndItsSplitTypes(List<ExpenseItemProjection> list) {
        Map<Integer, String> itemsAndItsSplitTypes = new HashMap<>();
        list.forEach(line -> itemsAndItsSplitTypes.putIfAbsent(line.getItemId(), line.getSplitType()));
        return itemsAndItsSplitTypes;
    }

    private void userIsOwedBalanceSplitEqually(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double userShare = tmp.getTotalPrice() / itemShareComponents.size();
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + userShare);
        }
    }

    private void userOwesBalanceSplitEqually(int itemShareComponentsSize, ExpenseItemProjection userOweItem,
                                             Map<String, Map<String, Double>> userDebtsBalance) {

        Map<String, Double> userBalance = userDebtsBalance.get(userOweItem.getPayer());
        Double userShare = userOweItem.getTotalPrice() / itemShareComponentsSize;
        Double existingAmount = userBalance.get(userOweItem.getCurrency());
        userBalance.put(userOweItem.getCurrency(), existingAmount + userShare);
    }

    private void userIsOwedBalanceSplitByShares(List<ExpenseItemProjection> itemShareComponents,
                                                Map<String, Map<String, Double>> userDebtsBalance) {
        Double shareSum = itemShareComponents
                .stream()
                .map(ExpenseItemProjection::getValue)
                .reduce(0.0, Double::sum);

        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double userShare = tmp.getTotalPrice() / shareSum * tmp.getValue();
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + userShare);
        }
    }

    private void userOwesBalanceSplitByShares(List<ExpenseItemProjection> itemShareComponents,
                                              ExpenseItemProjection userOweItem,
                                              Map<String, Map<String, Double>> userOweBalances) {
        Double shareSum = itemShareComponents
                .stream()
                .map(ExpenseItemProjection::getValue)
                .reduce(0.0, Double::sum);

        Map<String, Double> userBalance = userOweBalances.get(userOweItem.getPayer());
        Double userShare = userOweItem.getTotalPrice() / shareSum * userOweItem.getValue();
        Double existingAmount = userBalance.get(userOweItem.getCurrency());
        userBalance.put(userOweItem.getCurrency(), existingAmount + userShare);
    }

    private void userIsOwedBalanceSplitByPercentage(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double percentageAmount = tmp.getTotalPrice() / 100 * tmp.getValue();
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + percentageAmount);
        }
    }

    private void userOwesBalanceSplitByPercentage(ExpenseItemProjection userOweItem,
                                                  Map<String, Map<String, Double>> userOweBalances) {
        Map<String, Double> userBalance = userOweBalances.get(userOweItem.getPayer());
        Double userShare = userOweItem.getTotalPrice() / 100 * userOweItem.getValue();
        Double existingAmount = userBalance.get(userOweItem.getCurrency());
        userBalance.put(userOweItem.getCurrency(), existingAmount + userShare);
    }

    private void userIsOwedBalanceSplitByManuallyEnteredAmounts(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double existingAmount = userBalance.get(tmp.getCurrency());
            userBalance.put(tmp.getCurrency(), existingAmount + tmp.getValue());
        }
    }

    private void userOwesBalanceSplitByManuallyEnteredAmounts(ExpenseItemProjection userOweItem,
                                                              Map<String, Map<String, Double>> userOweBalances) {
        Map<String, Double> userBalance = userOweBalances.get(userOweItem.getPayer());
        Double existingAmount = userBalance.get(userOweItem.getCurrency());
        userBalance.put(userOweItem.getCurrency(), existingAmount + userOweItem.getValue());
    }

    @SneakyThrows
    private void userIsOwedBalanceSplitFeeAndTax(List<ExpenseItemProjection> expenseItems, Map<String, Map<String, Double>> userDebtsBalance) {
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

    @SneakyThrows
    private void userOwesBalanceSplitFeeAndTax(List<ExpenseItemProjection> expenseItems,
                                               Map<String, Map<String, Double>> userOweBalances) {
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

            Map<String, Double> userBalance = userOweBalances.get(expense.getPayer().getName());
            Double existingAmount = userBalance.get(expense.getCurrency());
            userBalance.put(expense.getCurrency(), existingAmount + feeAndTaxUserFraction);
        }
    }
}