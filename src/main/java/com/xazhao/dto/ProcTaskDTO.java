package com.xazhao.dto;

import com.xazhao.entity.ProcTask;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ActProcTaskDTO.java
 * @Author AnZhaoxu
 * @Create 2024.03.21
 * @UpdateUser
 * @UpdateDate 2024.03.21
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ProcTaskDTO extends ProcTask {

    private static final long serialVersionUID = 1L;

}
