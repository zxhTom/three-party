package com.github.zxhtom.message.api.model.material;

import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/7
 */
@Data
public class Meterial {
    //图片
    //钉钉 :图片，图片最大1MB。支持上传jpg、gif、png、bmp格式
    //微信  ：10M，支持PNG\JPEG\JPG\GIF格式
    protected static final String METERIAL_IMAGE="image";
    //语音
    //钉钉 : 语音，语音文件最大2MB。支持上传amr、mp3、wav格式
    //微信 ： 2M，播放长度不超过60s，支持AMR\MP3格式
    protected static final String METERIAL_VOICE="voice";

    //视频
    //钉钉: 视频，视频最大10MB。支持上传mp4格式。
    //微信： 10MB，支持MP4格式
    protected static final String METERIAL_VIDEO="video";

    //文件
    //仅钉钉支持 ： 普通文件，最大10MB。支持上传doc、docx、xls、xlsx、ppt、pptx、zip、pdf、rar格式
    protected static final String METERIAL_FILE="file";
    //缩略图
    //仅微信支持 ： 64KB，支持JPG格式
    protected static final String METERIAL_THUMB="thumb";

    private String type;

    public Meterial(String type) {
        this.type = type;
    }
}
