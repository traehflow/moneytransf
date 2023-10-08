package com.vsoft.moneytransf.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.vsoft.moneytransf.Roles;
import com.vsoft.moneytransf.service.MerchantsService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/import")
public class CSVImportController {

    final MerchantsService merchantsService;

    CSVImportController(MerchantsService merchantsService) {
        this.merchantsService = merchantsService;
    }

    @Operation(summary = "Import data from csv input", description = "Here's how input line is looking like: \n name,description,email,ENABLED/DISABLED")
    @PostMapping("/merchants")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<String> importMerchants(@RequestBody String csvData) {
        try {
                merchantsService.importMerchant(csvData);
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