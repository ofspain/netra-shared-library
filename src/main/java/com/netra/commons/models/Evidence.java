package com.netra.commons.models;

import com.netra.commons.enums.EvidenceActor;
import com.netra.commons.enums.EvidenceContext;
import com.netra.commons.enums.EvidenceType;
import lombok.Data;

@Data
public class Evidence extends BaseEntity {
    private EvidenceType evidenceType;
    private Long disputeId;

    private String uuid;           // Unique identifier (used in S3 key)
    private String s3Key;          // Full path/key in S3 (e.g. evidence/uuid.jpg)
    private String originalFilename;
    private String extension;      // jpg, png, pdf, etc.
    private String contentType;    // image/jpeg, application/pdf
    private Long size;             // Optional: in bytes
    private String remarks;                    // Free-text note or context message

    private String averageHash;  //used for duplication detection
    private String perpetualHash; //used for duplication detection
    private EvidenceActor uploadedByActor;
    private EvidenceContext evidenceContext;
    private String uploadedBy;//may be unique identifier of uploader if it can be determined

    public static final Integer PREFIX_SEARCH_LENGTH_FOR_HASH_DUPLICATE = 16;
}
