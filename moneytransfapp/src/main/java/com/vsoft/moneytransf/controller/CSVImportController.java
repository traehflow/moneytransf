package com.vsoft.moneytransf.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.service.impl.MerchantsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/import")
public class CSVImportController {

    final MerchantsServiceImpl merchantsService;

    CSVImportController(MerchantsServiceImpl merchantsService) {
        this.merchantsService = merchantsService;
    }

    @Operation(summary = "Import data from csv input", description = "Here's how input line is looking like: \n name,description,email,ENABLED/DISABLED")
    @PostMapping("/merchants")
    @Secured(Roles.ROLE_PREFIX + Roles.ADMIN)
    public ResponseEntity<String> importMerchants(@RequestParam("csvFile") MultipartFile csvFile) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            System.out.println(auth);
                merchantsService.importMerchant(csvFile.getInputStream());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error processing CSV data");
        } catch (CsvValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid input");
        }
        return ResponseEntity.ok("CSV data imported successfully");
    }
}