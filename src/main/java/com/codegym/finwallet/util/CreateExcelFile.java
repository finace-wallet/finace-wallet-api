package com.codegym.finwallet.util;

import com.codegym.finwallet.constant.ExcelFileConstant;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.entity.Transaction;
import com.codegym.finwallet.entity.Wallet;
import com.codegym.finwallet.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.codegym.finwallet.constant.ExcelFileConstant.EXCEL_FOLDER_PATH;

@Component
@RequiredArgsConstructor
public class CreateExcelFile {
    private final ProfileRepository profileRepository;

    public String createExcelFile(Map<Wallet, List<Transaction>> walletTransactionsMap) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        for (Map.Entry<Wallet, List<Transaction>> entry : walletTransactionsMap.entrySet()) {
            Wallet wallet = entry.getKey();
            List<Transaction> transactions = entry.getValue();

            Sheet sheet = workbook.createSheet(wallet.getName());

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Wallet name");
            headerRow.createCell(1).setCellValue("Amount");
            headerRow.createCell(2).setCellValue("Type");
            headerRow.createCell(3).setCellValue("Date");
            headerRow.createCell(4).setCellValue("Description");
            headerRow.createCell(5).setCellValue("Implementer");
            headerRow.createCell(6).setCellValue("Currency");
            int rowNum = 1;
            for (Transaction transaction : transactions) {
                String fullName = getFullNameFromProfile(transaction.getAppUser().getEmail());
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(transaction.getWallet().getName());
                row.createCell(1).setCellValue(transaction.getAmount());
                row.createCell(2).setCellValue(transaction.getTransactionCategory().getName());
                row.createCell(3).setCellValue(transaction.getTransactionDate());
                row.createCell(4).setCellValue(transaction.getDescription());
                row.createCell(5).setCellValue(fullName);
                row.createCell(6).setCellValue(transaction.getCurrency());
            }
        }

        String excelFileName = "transactions_" + System.currentTimeMillis() + ".xlsx";
        String excelFilePath = EXCEL_FOLDER_PATH + excelFileName;
        Path directoryPath = Paths.get(EXCEL_FOLDER_PATH);
        Files.createDirectories(directoryPath);
        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        }catch (IOException e){
            throw new RuntimeException("Error writing to Excel file: " + excelFilePath, e);
        }

        return excelFileName;
    }

    private String getFullNameFromProfile(String email){
        Optional<Profile> profileOptional = profileRepository.findProfileByEmail(email);
        if (profileOptional.isPresent()){
            Profile profile = profileOptional.get();
            return profile.getFullName();
        }
        return null;
    }
}
