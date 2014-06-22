package com.realaicy.pg.showcase.parentchild.entity;

/**
 * 父子表测试使用的enum
 * 枚举：父子表类型
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public enum ParentChildType {
    type1("测试类型1"), type2("测试类型2");

    private final String info;

    private ParentChildType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
