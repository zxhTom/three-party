package com.github.zxhtom.message.api.service;

import com.github.zxhtom.message.api.model.material.Meterial;
import com.github.zxhtom.message.api.model.material.MeterialResponse;

import java.io.InputStream;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/6
 */
public interface UploadService {
    public MeterialResponse uploadPic(Meterial meterial , String fileName, InputStream inputStream) ;
}
