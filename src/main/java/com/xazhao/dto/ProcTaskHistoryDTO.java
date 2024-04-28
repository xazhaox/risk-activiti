package com.xazhao.dto;

import com.xazhao.entity.ProcTaskHistory;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description Created on 2024/03/21.
 * @Author xaZhao
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProcTaskHistoryDTO extends ProcTaskHistory {

    private static final long serialVersionUID = 1L;

}
