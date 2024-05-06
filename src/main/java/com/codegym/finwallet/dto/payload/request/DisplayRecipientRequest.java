package com.codegym.finwallet.dto.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DisplayRecipientRequest {

    private int page = 0;
    private int size = 5;
    private String transferEmail;

}
