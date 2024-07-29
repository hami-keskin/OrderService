package com.example.orders.controller;

import com.example.orders.dto.InvoiceDto;
import com.example.orders.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public List<InvoiceDto> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @GetMapping("/{id}")
    public InvoiceDto getInvoiceById(@PathVariable Integer id) {
        return invoiceService.getInvoiceById(id);
    }

    @PostMapping
    public InvoiceDto createInvoice(@RequestBody InvoiceDto invoiceDto) {
        return invoiceService.createInvoice(invoiceDto);
    }

    @PutMapping("/{id}")
    public InvoiceDto updateInvoice(@PathVariable Integer id, @RequestBody InvoiceDto invoiceDto) {
        return invoiceService.updateInvoice(id, invoiceDto);
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable Integer id) {
        invoiceService.deleteInvoice(id);
    }
}
