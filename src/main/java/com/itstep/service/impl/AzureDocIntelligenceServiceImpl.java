package com.itstep.service.impl;

import com.azure.ai.documentintelligence.DocumentIntelligenceClient;
import com.azure.ai.documentintelligence.models.*;
import com.azure.core.util.polling.SyncPoller;
import com.itstep.dto.ExpenseDto;
import com.itstep.dto.ItemDto;
import com.itstep.service.AzureDocIntelligenceService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.azure.ai.documentintelligence.models.DocumentFieldType.*;
import static com.itstep.dto.ItemDto.createItem;

@Service
@AllArgsConstructor
public class AzureDocIntelligenceServiceImpl implements AzureDocIntelligenceService {

    private final Double CONFIDENCE_LEVEL_THRESHOLD = 0.90;

    private DocumentIntelligenceClient documentIntelligenceClient;

    public ExpenseDto getInfoFromImage(byte[] image) {
        AnalyzeResult result = callDocumentIntelligenceService(image);
        return parseResult(result);
    }

    @SneakyThrows
    private AnalyzeResult callDocumentIntelligenceService(byte[] image) {

        SyncPoller<AnalyzeResultOperation, AnalyzeResult> poller =
                documentIntelligenceClient.beginAnalyzeDocument(
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
        List<Document> analyzedInvoices = result.getDocuments();
        ExpenseDto expenseDto = new ExpenseDto();

        for (Document analyzedInvoice : analyzedInvoices) {
            Map<String, DocumentField> invoiceFields = analyzedInvoice.getFields();

            DocumentField subTotalField = invoiceFields.get("Subtotal");
            if (subTotalField != null) {
                if (CURRENCY == subTotalField.getType()
                        && subTotalField.getConfidence() >= CONFIDENCE_LEVEL_THRESHOLD) {
                    expenseDto.setSubtotalAmount(subTotalField.getValueCurrency().getAmount());
                }
            }

            DocumentField totalField = invoiceFields.get("Total");
            if (totalField != null && totalField.getConfidence() >= CONFIDENCE_LEVEL_THRESHOLD) {
                if (CURRENCY == totalField.getType()) {
                    expenseDto.setTotalAmount(totalField.getValueCurrency().getAmount());
                    expenseDto.setCurrency(totalField.getValueCurrency().getCurrencyCode());
                }
            }

            DocumentField transactionDateField = invoiceFields.get("TransactionDate");
            if (transactionDateField != null && transactionDateField.getConfidence() >= CONFIDENCE_LEVEL_THRESHOLD) {
                if (DocumentFieldType.DATE == transactionDateField.getType()) {
                    expenseDto.setTransactionDate(transactionDateField.getValueDate());
                }
            }

            DocumentField transactionTimeField = invoiceFields.get("TransactionTime");
            if (transactionTimeField != null && transactionTimeField.getConfidence() >= CONFIDENCE_LEVEL_THRESHOLD) {
                if (DocumentFieldType.TIME == transactionTimeField.getType()) {
                    expenseDto.setTransactionTime(transactionTimeField.getValueTime());
                }
            }

            DocumentField invoiceItemsField = invoiceFields.get("Items");

            if (invoiceItemsField == null) {
                return expenseDto;
            }

            List<Map<String, DocumentField>> invoiceItems = invoiceItemsField.getValueArray()
                    .stream()
                    .map(DocumentField::getValueObject)
                    .toList();

            invoiceItems.forEach(item -> {

                ItemDto itemDto = createItem();

                item.forEach((key, documentField) -> {

                    if (documentField.getConfidence() >= CONFIDENCE_LEVEL_THRESHOLD) {

                        if ("Description".equals(key) && STRING == documentField.getType()) {
                            itemDto.setDescription(
                                    documentField.getContent()
                                            .replaceAll("\n", " ")
                                            .replaceAll("[^\\p{L} ]", "")
                            );
                        }

                        if ("Price".equals(key) && CURRENCY == documentField.getType()) {
                            itemDto.setPrice(documentField.getValueCurrency().getAmount());
                        }

                        if ("Quantity".equals(key) && NUMBER == documentField.getType()) {
                            itemDto.setQuantity(documentField.getValueNumber());
                        }

                        if ("TotalPrice".equals(key) && CURRENCY == documentField.getType()) {
                            itemDto.setTotalPrice(documentField.getValueCurrency().getAmount());
                        }
                    }
                });

                expenseDto.getItems().add(itemDto);
            });
        }

        return expenseDto;
    }
}