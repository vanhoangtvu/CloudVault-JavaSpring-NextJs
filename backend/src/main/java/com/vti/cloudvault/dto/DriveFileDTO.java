package com.vti.cloudvault.dto;


public class DriveFileDTO {
    private String id;
    private String name;
    private String mimeType;
    private Long size;

    public DriveFileDTO() {}

    public DriveFileDTO(String id, String name, String mimeType, Long size) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
