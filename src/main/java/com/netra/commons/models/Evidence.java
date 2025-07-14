package com.netra.commons.models;

import com.netra.commons.models.enums.EvidenceType;

public class Evidence extends BaseEntity {
    private EvidenceType evidenceType;
    private String disputeCode;

    private String uuid;           // Unique identifier (used in S3 key)
    private String s3Key;          // Full path/key in S3 (e.g. evidence/uuid.jpg)
    private String originalFilename;
    private String extension;      // jpg, png, pdf, etc.
    private String contentType;    // image/jpeg, application/pdf
    private Long size;             // Optional: in bytes
}
