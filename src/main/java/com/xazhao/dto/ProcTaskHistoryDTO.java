package com.xazhao.dto;

import com.xazhao.entity.ProcTaskHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ActProcTaskHistoryDTO.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProcTaskHistoryDTO extends ProcTaskHistory {

    private static final long serialVersionUID = 1L;

}
