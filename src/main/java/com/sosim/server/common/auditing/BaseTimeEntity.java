package com.sosim.server.common.auditing;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
public class BaseTimeEntity {

    @Column(name = "CREATED_DATE", updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
