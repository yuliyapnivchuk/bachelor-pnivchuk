package com.itstep.service.impl;

import com.itstep.dto.BalanceDto;
import com.itstep.entity.Expense;
import com.itstep.exception.ExpenseNotFound;
import com.itstep.entity.ExpenseItemProjection;
import com.itstep.repository.ExpenseRepository;
import com.itstep.service.BalanceService;
import com.itstep.service.SplitType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.itstep.exception.ConstantsUtility.EXPENSE_WITH_SUCH_ID_NOT_FOUND;

@Service
@AllArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private ExpenseRepository expenseRepository;

    public BalanceDto getBalance(String user) {
        Map<String, Map<String, Double>> userIsOwedBalance = calculateWhatUserIsOwed(user);
        Map<String, Map<String, Double>> userOweBalance = calculateWhatUserOwes(user);
        Map<String, Double> userIsOwedTotal = calculateTotal(userIsOwedBalance);
        Map<String, Double> userOwesTotal = calculateTotal(userOweBalance);
        Map<String, Double> totalBalance = calculateTotal(userIsOwedTotal, userOwesTotal);

        simplifyUserBalances(userOweBalance, userIsOwedBalance);

        return BalanceDto.builder()
                .userIsOwed(userIsOwedBalance)
                .userOwes(userOweBalance)
                .userIsOwedTotal(userIsOwedTotal)
                .userOwesTotal(userOwesTotal)
                .totalBalance(totalBalance)
                .build();
    }

    private void simplifyUserBalances(Map<String, Map<String, Double>> userOweBalance,
                                           Map<String, Map<String, Double>> userIsOwedBalance) {
        for (String userKey : userOweBalance.keySet()) {
            if (!userIsOwedBalance.containsKey(userKey)) {
                continue;
            }

            Map<String, Double> oweMap = userOweBalance.get(userKey);
            Map<String, Double> owedMap = userIsOwedBalance.get(userKey);

            Iterator<Map.Entry<String, Double>> oweIterator = oweMap.entrySet().iterator();

            while (oweIterator.hasNext()) {
                Map.Entry<String, Double> oweEntry = oweIterator.next();
                String currency = oweEntry.getKey();
                double oweAmount = oweEntry.getValue();

                if (!owedMap.containsKey(currency)) {
                    continue;
                }

                double owedAmount = owedMap.get(currency);

                if (oweAmount > owedAmount) {
                    oweEntry.setValue(oweAmount - owedAmount);
                    owedMap.remove(currency);
                } else if (oweAmount < owedAmount) {
                    owedMap.put(currency, owedAmount - oweAmount);
                    oweIterator.remove();
                } else {
                    oweIterator.remove();
                    owedMap.remove(currency);
                }
            }
        }
    }

    private Map<String, Map<String, Double>> calculateWhatUserIsOwed(String user) {
        List<ExpenseItemProjection> list = expenseRepository.findUserIsOwedItems(user);

        Map<String, Map<String, Double>> userIsOwedBalance = initUserIsOwedBalanceMap(list);
        Map<String, String> itemsAndItsSplitTypes = getItemsAndItsSplitTypes(list);

        for (Map.Entry<String, String> expenseItems : itemsAndItsSplitTypes.entrySet()) {

            List<ExpenseItemProjection> itemShareComponents = list
                    .stream()
                    .filter(i -> (i.getExpenseId() + "-" + i.getItemId()).equals(expenseItems.getKey()))
                    .toList();

            switch (SplitType.get(expenseItems.getValue())) {
                case EQUAL -> userIsOwedBalanceSplitEqually(itemShareComponents, userIsOwedBalance);
                case SHARES -> userIsOwedBalanceSplitByShares(itemShareComponents, userIsOwedBalance);
                case PERCENTAGE -> userIsOwedBalanceSplitByPercentage(itemShareComponents, userIsOwedBalance);
                case MANUAL -> userIsOwedBalanceSplitByExactAmounts(itemShareComponents, userIsOwedBalance);
            }
        }

        userIsOwedBalanceSplitFeeAndTax(list, userIsOwedBalance);
        userIsOwedBalance.remove(user);
        return userIsOwedBalance;
    }

    private Map<String, Map<String, Double>> calculateWhatUserOwes(String user) {
        List<ExpenseItemProjection> list = expenseRepository.findUserOweItems(user);

        Map<String, Map<String, Double>> userOweBalances = initUserOwesBalanceMap(list);
        Map<String, String> itemsAndItsSplitTypes = getItemsAndItsSplitTypes(list);

        for (Map.Entry<String, String> expenseItems : itemsAndItsSplitTypes.entrySet()) {

            List<ExpenseItemProjection> itemShareComponents = list
                    .stream()
                    .filter(i -> (i.getExpenseId() + "-" + i.getItemId()).equals(expenseItems.getKey()))
                    .toList();

            Optional<ExpenseItemProjection> userOweItem = itemShareComponents
                    .stream()
                    .filter(i -> i.getUserName().equals(user))
                    .findFirst();

            if (userOweItem.isEmpty()) {
                continue;
            }

            switch (SplitType.get(expenseItems.getValue())) {
                case EQUAL ->
                        userOwesBalanceSplitEqually(itemShareComponents.size(), userOweItem.get(), userOweBalances);
                case SHARES -> userOwesBalanceSplitByShares(itemShareComponents, userOweItem.get(), userOweBalances);
                case PERCENTAGE -> userOwesBalanceSplitByPercentage(userOweItem.get(), userOweBalances);
                case MANUAL -> userOwesBalanceSplitByExactAmounts(userOweItem.get(), userOweBalances);
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

    private Map<String, String> getItemsAndItsSplitTypes(List<ExpenseItemProjection> list) {
        Map<String, String> itemsAndItsSplitTypes = new HashMap<>();
        list.forEach(line -> itemsAndItsSplitTypes
                .putIfAbsent(line.getExpenseId() + "-" + line.getItemId(), line.getSplitType()));
        return itemsAndItsSplitTypes;
    }

    private void userIsOwedBalanceSplitEqually(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double userShare = tmp.getTotalPrice() / itemShareComponents.size();
            userBalance.merge(tmp.getCurrency(), userShare, Double::sum);
        }
    }

    private void userOwesBalanceSplitEqually(int itemShareComponentsSize, ExpenseItemProjection userOweItem,
                                             Map<String, Map<String, Double>> userDebtsBalance) {

        Map<String, Double> userBalance = userDebtsBalance.get(userOweItem.getPayer());
        Double userShare = userOweItem.getTotalPrice() / itemShareComponentsSize;
        userBalance.merge(userOweItem.getCurrency(), userShare, Double::sum);
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
            userBalance.merge(tmp.getCurrency(), userShare, Double::sum);
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
        userBalance.merge(userOweItem.getCurrency(), userShare, Double::sum);
    }

    private void userIsOwedBalanceSplitByPercentage(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            Double percentageAmount = tmp.getTotalPrice() / 100 * tmp.getValue();
            userBalance.merge(tmp.getCurrency(), percentageAmount, Double::sum);
        }
    }

    private void userOwesBalanceSplitByPercentage(ExpenseItemProjection userOweItem,
                                                  Map<String, Map<String, Double>> userOweBalances) {
        Map<String, Double> userBalance = userOweBalances.get(userOweItem.getPayer());
        Double userShare = userOweItem.getTotalPrice() / 100 * userOweItem.getValue();
        userBalance.merge(userOweItem.getCurrency(), userShare, Double::sum);
    }

    private void userIsOwedBalanceSplitByExactAmounts(List<ExpenseItemProjection> itemShareComponents, Map<String, Map<String, Double>> userDebtsBalance) {
        for (ExpenseItemProjection tmp : itemShareComponents) {
            Map<String, Double> userBalance = userDebtsBalance.get(tmp.getUserName());
            userBalance.merge(tmp.getCurrency(), tmp.getValue(), Double::sum);
        }
    }

    private void userOwesBalanceSplitByExactAmounts(ExpenseItemProjection userOweItem,
                                                    Map<String, Map<String, Double>> userOweBalances) {
        Map<String, Double> userBalance = userOweBalances.get(userOweItem.getPayer());
        userBalance.merge(userOweItem.getCurrency(), userOweItem.getValue(), Double::sum);
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
                userBalance.merge(expense.getCurrency(), feeAndTaxUserFraction, Double::sum);
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
            userBalance.merge(expense.getCurrency(), feeAndTaxUserFraction, Double::sum);
        }
    }

    private Map<String, Double> calculateTotal(Map<String, Map<String, Double>> userOweBalance) {
        Map<String, Double> userOwesTotal = new HashMap<>();
        for (Map<String, Double> userMap : userOweBalance.values()) {
            for (Map.Entry<String, Double> entry : userMap.entrySet()) {
                String currency = entry.getKey();
                Double amount = entry.getValue();
                userOwesTotal.merge(currency, amount, Double::sum);
            }
        }
        return userOwesTotal;
    }

    private Map<String, Double> calculateTotal(Map<String, Double> userIsOwedTotal, Map<String, Double> userOwesTotal) {
        Map<String, Double> totalBalance = new HashMap<>(userIsOwedTotal);
        userOwesTotal.forEach((currency, amount) -> totalBalance.merge(currency, -amount, Double::sum));
        return totalBalance;
    }
}