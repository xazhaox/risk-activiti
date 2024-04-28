package com.xazhao.utils;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description Created on 2024/03/22.
 * @Author xaZhao
 */

public class MultipartFileUtil {

    /**
     * InputStream转为MultipartFile
     *
     * @param input       InputStream流
     * @param orgFileName 文件名
     * @return MultipartFile
     * @throws IOException IO异常
     */
    public static MultipartFile convert(InputStream input, String orgFileName) throws IOException {

        return new MockMultipartFile(orgFileName, orgFileName, MediaType.MULTIPART_FORM_DATA_VALUE, input);
    }
}
