package com.realaicy.pg.core.plugin.entity;

/**
 * <p>实体实现该接口，表示需要进行状态管理
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface Stateable<T extends Enum<? extends Stateable.Status>> {

    public T getStatus();

    public void setStatus(T status);

    /**
     * 审核状态
     */
    public static enum AuditStatus implements Status {
        waiting("等待审核"), fail("审核失败"), success("审核成功");
        private final String info;

        private AuditStatus(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    /**
     * 是否显示
     */
    public static enum ShowStatus implements Status {
        hide("不显示"), show("显示");
        private final String info;

        private ShowStatus(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    /**
     * 标识接口，所有状态实现，需要实现该状态接口
     */
    public static interface Status {
    }
}
