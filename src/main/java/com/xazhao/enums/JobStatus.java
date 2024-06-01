package com.xazhao.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * @Description Created on 2024/04/26.
 * @Author xaZhao
 */

@Getter
@AllArgsConstructor
public enum JobStatus {
	/**/
	SIGN("SIGN", "会签"),
	ORSIGNED("ORSIGNED", "或签"),
	PENDING("PENDING", "委派");

	private final String code;
	private final String desc;

	public static String getJob(String code) {
		for (JobStatus e : JobStatus.values()) {
			if (e.code.equals(code)) {
				return e.desc;
			}
		}
		return null;
	}
} 