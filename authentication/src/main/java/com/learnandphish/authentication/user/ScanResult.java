package com.learnandphish.authentication.user;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "spiderfoot_scans")
public class ScanResult {
    @Id
    @Column(name = "target")
    private String target;
    
    @Column(name = "id")
    private UUID id;
    
    @Column(name = "modules")
    private String modules;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "result", columnDefinition = "jsonb")
    private String result;
    
    @Column(name = "spiderfoot_scan_id")
    private String spiderfootScanId;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
