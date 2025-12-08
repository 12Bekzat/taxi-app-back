package com.taxi.app.dtos;

import com.taxi.app.models.DocumentSide;
import com.taxi.app.models.DocumentType;
import lombok.Data;

@Data
public class DriverDocumentDto {
    private Long id;
    private DocumentType documentType;
    private DocumentSide side;
    private String url;
    private String status; // например PENDING/APPROVED/REJECTED, пока можно не заполнять
}
