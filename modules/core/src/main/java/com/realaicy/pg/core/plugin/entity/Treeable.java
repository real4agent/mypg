package com.realaicy.pg.core.plugin.entity;

import java.io.Serializable;

/**
 * <p>实体实现该接口表示想要实现树结构
 * <p/>
 *
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
public interface Treeable<ID extends Serializable> {

    public String getName();

    public void setName(String name);

    /**
     * 显示的图标 大小为16×16
     */
    public String getIcon();

    public void setIcon(String icon);

    /**
     * 父路径
     */
    public ID getParentId();

    public void setParentId(ID parentId);

    /**
     * 所有父路径 如1,2,3,
     */
    public String getParentIds();

    public void setParentIds(String parentIds);

    /**
     * 获取 parentIds 之间的分隔符
     */
    public String getSeparator();

    /**
     * 把自己构造出新的父节点路径
     */
    public String makeSelfAsNewParentIds();

    /**
     * 权重 用于排序 越小越排在前边
     */
    public Integer getWeight();

    public void setWeight(Integer weight);

    /**
     * 是否是根节点
     */
    public boolean isRoot();

    /**
     * 是否是叶子节点
     */
    public boolean isLeaf();

    /**
     * 是否有孩子节点
     */
    public boolean isHasChildren();

    /**
     * 根节点默认图标 如果没有默认 空即可  大小为16×16
     */
    public String getRootDefaultIcon();

    /**
     * 树枝节点默认图标 如果没有默认 空即可  大小为16×16
     */
    public String getBranchDefaultIcon();

    /**
     * 树叶节点默认图标 如果没有默认 空即可  大小为16×16
     */
    public String getLeafDefaultIcon();

}
