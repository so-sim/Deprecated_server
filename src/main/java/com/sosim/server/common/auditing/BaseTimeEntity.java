package com.sosim.server.common.auditing;

import java.time.LocalDateTime;
import javax.persistence.*;

import com.sosim.server.type.StatusType;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @Column(name = "CREATE_DATE", updatable = false)
    @CreatedDate
    private LocalDateTime createDate;

    @Column(name = "UPDATE_DATE")
    @LastModifiedDate
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_TYPE")
    protected StatusType statusType;

    @Column(name = "DELETE_DATE")
    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime deleteDate;

    public void delete() {
        statusType = StatusType.DELETED;
        deleteDate = LocalDateTime.now();
    }
}
