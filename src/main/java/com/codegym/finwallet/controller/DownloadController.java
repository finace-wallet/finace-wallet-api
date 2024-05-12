package com.codegym.finwallet.controller;

import com.codegym.finwallet.constant.ExcelFileConstant;
import com.codegym.finwallet.util.CreateExcelFile;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/download")
@RequiredArgsConstructor
public class DownloadController {

    @Autowired
    private CreateExcelFile createExcelFile;

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        File folder = new File(ExcelFileConstant.EXCEL_FOLDER_PATH);
        File file = new File(folder, fileName);

        if (!file.exists() || !file.getCanonicalPath().startsWith(folder.getCanonicalPath() + File.separator)) {
            return ResponseEntity.notFound().build();
        }
        Tika tika = new Tika();
        String mediaTypeString = tika.detect(file);
        MediaType mediaType = MediaType.parseMediaType(mediaTypeString);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(mediaType);

        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(resource);
    }
}
