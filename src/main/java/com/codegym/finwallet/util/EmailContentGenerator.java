package com.codegym.finwallet.util;

import com.codegym.finwallet.constant.MailConstant;
import com.codegym.finwallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailContentGenerator {
    private final TemplateEngine templateEngine;

    public String generateEmailContent(String email, List<Wallet> wallets, String excelFileName) {

        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("wallets", wallets);
        context.setVariable("intro", MailConstant.EMAIL_INTRO);
        context.setVariable("closing", MailConstant.EMAIL_CLOSING);
        context.setVariable("excelFileName", excelFileName);

        return templateEngine.process("report_expense_mail_template", context);
    }
}
