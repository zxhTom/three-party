package com.github.zxhtom.message.api.model.material;

import com.github.zxhtom.message.api.annations.Trans;
import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2022/1/7
 */
@Data
public class MeterialResponse {
    @Trans("errcode")
    private Long errCode;
    @Trans("errmsg")
    private String errMsg;
    @Trans("type")
    private String type;
    @Trans("media_id")
    private String mediaId;
    @Trans("create_at")
    private String createdAt;
}
