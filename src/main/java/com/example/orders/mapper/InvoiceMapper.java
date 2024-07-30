package com.example.orders.mapper;

import com.example.orders.dto.InvoiceDto;
import com.example.orders.entity.Invoice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDto toInvoiceDto(Invoice invoice);
    Invoice toInvoice(InvoiceDto invoiceDto);
    List<InvoiceDto> toInvoiceDtos(List<Invoice> invoices);
}
