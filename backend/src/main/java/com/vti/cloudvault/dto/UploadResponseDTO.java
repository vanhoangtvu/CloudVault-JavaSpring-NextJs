package com.vti.cloudvault.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDTO {
    private Long fileId;
    private String fileName;
    private String driveFileId;
    private String webViewLink;
    private String downloadLink;
    private Long size;
    private String sizeFormatted;
    private String message;
}
