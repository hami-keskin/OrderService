package com.example.orders.service;

import com.example.orders.dto.InvoiceDto;
import com.example.orders.entity.Invoice;
import com.example.orders.entity.Order;
import com.example.orders.mapper.InvoiceMapper;
import com.example.orders.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public List<InvoiceDto> getAllInvoices() {
        return invoiceRepository.findAll().stream()
                .map(InvoiceMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    public InvoiceDto getInvoiceById(Integer id) {
        return invoiceRepository.findById(id)
                .map(InvoiceMapper.INSTANCE::toDto)
                .orElse(null);
    }

    @Transactional
    public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
        Invoice invoice = InvoiceMapper.INSTANCE.toEntity(invoiceDto);
        return InvoiceMapper.INSTANCE.toDto(invoiceRepository.save(invoice));
    }

    @Transactional
    public InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        Order order = new Order();
        order.setId(invoiceDto.getOrderId());
        invoice.setOrder(order);
        invoice.setInvoiceDate(invoiceDto.getInvoiceDate());
        invoice.setTotalAmount(invoiceDto.getTotalAmount());
        return InvoiceMapper.INSTANCE.toDto(invoiceRepository.save(invoice));
    }

    @Transactional
    public void deleteInvoice(Integer id) {
        invoiceRepository.deleteById(id);
    }
}
