package com.example.orders.service;

import com.example.orders.dto.InvoiceDto;
import com.example.orders.entity.Invoice;
import com.example.orders.mapper.InvoiceMapper;
import com.example.orders.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(invoiceMapper::toInvoiceDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto getInvoiceById(Integer id) {
        return invoiceRepository.findById(id)
                .map(invoiceMapper::toInvoiceDto)
                .orElse(null);
    }

    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceMapper.toInvoice(invoiceDto);
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceDto(invoice);
    }

    public InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto) {
        return invoiceRepository.findById(id)
                .map(existingInvoice -> {
                    existingInvoice.setOrderId(invoiceDto.getOrderId());
                    existingInvoice.setInvoiceDate(invoiceDto.getInvoiceDate());
                    existingInvoice.setTotalAmount(invoiceDto.getTotalAmount());
                    return invoiceMapper.toInvoiceDto(invoiceRepository.save(existingInvoice));
                })
                .orElse(null);
    }

    public boolean deleteInvoice(Integer id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
