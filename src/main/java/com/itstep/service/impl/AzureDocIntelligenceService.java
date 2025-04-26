package com.itstep.service.impl;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.models.*;
import com.azure.core.util.polling.SyncPoller;
import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ItemDto;
import com.itstep.service.ScanReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.azure.ai.documentintelligence.models.DocumentFieldType.*;
import static com.itstep.dto.ItemDto.createItem;

@Service
@AllArgsConstructor
public class AzureDocIntelligenceService implements ScanReceiptService {

    private final Double CONFIDENCE_LEVEL_THRESHOLD = 0.90;

    private DocumentIntelligenceClient client;

    public ExpenseDto scanReceipt(byte[] image) {
        AnalyzeResult result = callService(image);
        return parseResult(result);
    }

    private AnalyzeResult callService(byte[] image) {

        SyncPoller<AnalyzeResultOperation, AnalyzeResult> poller = client.beginAnalyzeDocument(
                "prebuilt-receipt",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                new AnalyzeDocumentRequest().setBase64Source(image)
        );

        return poller.getFinalResult();
    }

    private ExpenseDto parseResult(AnalyzeResult result) {
        ExpenseDto expenseDto = new ExpenseDto();
        Document analyzedInvoice = result.getDocuments().getFirst();
        Map<String, DocumentField> invoiceFields = analyzedInvoice.getFields();
        DocumentField subTotalField = invoiceFields.get("Subtotal");
        DocumentField totalField = invoiceFields.get("Total");
        DocumentField transactionDateField = invoiceFields.get("TransactionDate");
        DocumentField transactionTimeField = invoiceFields.get("TransactionTime");
        DocumentField invoiceItemsField = invoiceFields.get("Items");

        if (isValidField(subTotalField) && CURRENCY == subTotalField.getType()) {
            expenseDto.setSubtotalAmount(subTotalField.getValueCurrency().getAmount());
        }

        if (isValidField(totalField) && CURRENCY == totalField.getType()) {
            expenseDto.setTotalAmount(totalField.getValueCurrency().getAmount());
            expenseDto.setCurrency(totalField.getValueCurrency().getCurrencyCode());
        }

        if (isValidField(transactionDateField) && DATE == transactionDateField.getType()) {
            expenseDto.setTransactionDate(transactionDateField.getValueDate());
        }

        if (isValidField(transactionTimeField) && TIME == transactionTimeField.getType()) {
            expenseDto.setTransactionTime(transactionTimeField.getValueTime());
        }

        if (invoiceItemsField.getValueArray().isEmpty()) {
            return expenseDto;
        }

        List<ItemDto> expenseItems = parseInvoiceItems(invoiceItemsField);
        expenseDto.setItems(expenseItems);

        return expenseDto;
    }

    private List<ItemDto> parseInvoiceItems(DocumentField invoiceItemsField) {
        List<ItemDto> expenseItems = new ArrayList<>();

        List<Map<String, DocumentField>> invoiceItems = invoiceItemsField.getValueArray()
                .stream()
                .map(DocumentField::getValueObject)
                .toList();

        for (Map<String, DocumentField> item : invoiceItems) {
            ItemDto itemDto = createItem();

            for (Map.Entry<String, DocumentField> itemAttribute : item.entrySet()) {
                DocumentField documentField = itemAttribute.getValue();

                if (documentField.getConfidence() <= CONFIDENCE_LEVEL_THRESHOLD) {
                    continue;
                }

                switch (itemAttribute.getKey()) {
                    case "Description" -> {
                        String value = (STRING == documentField.getType()) ? deleteSpecChars(documentField.getContent()) : null;
                        itemDto.setDescription(value);
                    }
                    case "Price" -> {
                        Double price = (CURRENCY == documentField.getType() && documentField.getValueCurrency() != null)
                                ? documentField.getValueCurrency().getAmount() : null;
                        itemDto.setPrice(price);
                    }
                    case "Quantity" -> {
                        Double quantity = (NUMBER == documentField.getType()) ? documentField.getValueNumber() : null;
                        itemDto.setQuantity(quantity);
                    }
                    case "TotalPrice" -> {
                        Double totalPrice = (CURRENCY == documentField.getType() && documentField.getValueCurrency() != null)
                                ? documentField.getValueCurrency().getAmount() : null;
                        itemDto.setTotalPrice(totalPrice);
                    }
                }
            }
            expenseItems.add(itemDto);
        }
        return expenseItems;
    }

    private boolean isValidField(DocumentField field) {
        return field != null && field.getConfidence() >= CONFIDENCE_LEVEL_THRESHOLD;
    }

    private String deleteSpecChars(String str) {
        return str.replaceAll("\n", " ")
                .replaceAll("[^\\p{L} ]", "");
    }
}