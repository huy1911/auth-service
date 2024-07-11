package com.lpb.mid.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Data
@Table(name = "ESB_LOGGING")
public class EsbLogging {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "APP_ID")
    private String appId;
    @Column(name = "REFERENCE_ID")
    private String referenceId;
    @Column(name = "SOURCE_ENV")

    private String sourceEnv;
    @Column(name = "REQUEST_MSG")

    private String requestMsg;
    @Column(name = "RESPONSE_MSG")

    private String responseMsg;
    @Column(name = "CORE_REF_NO")

    private String coreRefNo;
    @Column(name = "ERROR")

    private String error;
    @Column(name = "END_DATE")

    private Instant endDate;
    @Column(name = "DUE_TIME")

    private String dueTime;
    @Column(name = "STARTED_DATE")

    private Instant startedDate;
    @Column(name = "TYPE")

    private String type;
    @Column(name = "SOURCE_SYSTEM")

    private String sourceSystem;
    @Column(name = "DESTINATION")

    private String destination;


}
