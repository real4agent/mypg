package com.realaicy.pg.core.entity;

/**
 * <p>学校类型</p>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public enum SchoolType {
    primary_school("小学"), secondary_school("中学"), high_school("高中"), university("大学");

    private final String info;

    private SchoolType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
