package com.vti.cloudvault.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDTO {
    private Long id;
    private String fileName;
    private String mimeType;
    private String webViewLink;
    private String downloadLink;
    private Long size;
    private String sizeFormatted;
    private LocalDateTime createdAt;
    private String username;
    private boolean canEdit;
    private boolean canDelete;
}
