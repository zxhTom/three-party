package com.github.zxhtom.dingding.core.model;

import com.github.zxhtom.message.api.annations.Trans;
import lombok.Data;

/**
 * TODO
 *
 * @author zxhtom
 * 2021/12/30
 */
@Data
public class Dept {
    @Trans("dept_id")
    private Long deptId;
    private String name;
    @Trans("parent_id")
    private Long parentId;
    @Trans("create_dept_group")
    private boolean createDeptGroup;
    @Trans("auto_add_user")
    private boolean autoAddUser;
}
