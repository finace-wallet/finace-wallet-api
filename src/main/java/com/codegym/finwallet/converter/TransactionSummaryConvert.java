package com.codegym.finwallet.converter;

import com.codegym.finwallet.dto.payload.response.TransactionSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionSummaryConvert {

    public TransactionSummaryResponse convertToResponse(List<Object[]> objectArrays) {
        if (objectArrays != null && !objectArrays.isEmpty()) {
            Object[] array = objectArrays.get(0);
            Long transactionTotal = (Long) array[0];
            Double amountTotal = (Double) array[1];

            TransactionSummaryResponse response = new TransactionSummaryResponse();
            response.setTransactionTotal(transactionTotal);
            response.setAmountTotal(amountTotal);

            return response;
        }
        return null;
    }
}
