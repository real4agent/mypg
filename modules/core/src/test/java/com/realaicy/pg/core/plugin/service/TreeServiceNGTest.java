package com.realaicy.pg.core.plugin.service;

import com.realaicy.pg.core.plugin.entity.Tree;
import com.realaicy.pg.core.test.BaseTransactionNGTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

/**
 * @author realaicy
 * @version 1.1
 * @email realaicy@gmail.com
 * @qq 8042646
 * @date 14-2-1 上午9:18
 * @description TODO
 * @since 1.1
 */
@SuppressWarnings("UnusedDeclaration")
public class TreeServiceNGTest extends BaseTransactionNGTest {
    @Autowired
    private TreeService treeService;

    private Tree root = null;

    //不可以设置为BeforeTest，因为Spring内部还没有支持TestNG的这个注解
    @BeforeMethod
    public void setUp() {
        root = createTree(0, "0/");
    }

    @Test
    /**
     *   0->source
     *    ->target
     *    ---------------------------------
     *    0->target
     *             ->source
     */
    public void testMoveAsChildWithNoChild() {
        Tree source = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree target = createTree(root.getId(), root.makeSelfAsNewParentIds());
        flush();
        treeService.move(source, target, "inner");
        clear();
        assertEquals(target.getId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.makeSelfAsNewParentIds(), treeService.findOne(source.getId()).getParentIds());
        assertEquals(Integer.valueOf(1), treeService.findOne(source.getId()).getWeight());
    }

    /**
     * 0->source
     * -------->target
     * -------->t
     * ---------------------------------
     * 0->target
     * -------->t
     * --------->source
     */
    @Test
    public void testMoveAsChildWithChild() {
        Tree source = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree target = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree t = createTree(target.getId(), target.getParentIds());
        treeService.move(source, target, "inner");
        clear();
        assertEquals(target.getId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.makeSelfAsNewParentIds(), treeService.findOne(source.getId()).getParentIds());

        assertEquals(Integer.valueOf(2), treeService.findOne(source.getId()).getWeight());
    }

    /**
     * 0->target
     * "       "-------->t
     * ->source
     * <p/>
     * ---------------------------------
     * 0->target
     * -------->t
     * --------->source
     */
    @Test
    public void testMoveAsBeforeWithNoPrevSiblings() {
        Tree target = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldTargetWeight = target.getWeight();
        Tree t = createTree(target.getId(), target.makeSelfAsNewParentIds());
        Tree source = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldSourceWeight = source.getWeight();
        treeService.move(source, target, "prev");
        clear();

        assertEquals(target.getParentId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.getParentIds(), treeService.findOne(source.getId()).getParentIds());

        assertEquals(Integer.valueOf(oldTargetWeight), treeService.findOne(source.getId()).getWeight());
        assertEquals(Integer.valueOf(oldSourceWeight), treeService.findOne(target.getId()).getWeight());

    }

    @Test
    public void testMoveAsBeforeWithPrevSiblings() {
        Tree t = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree target = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldTargetWeight = target.getWeight();
        Tree source = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldSourceWeight = source.getWeight();
        treeService.move(source, target, "prev");

        clear();

        assertEquals(target.getParentId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.getParentIds(), treeService.findOne(source.getId()).getParentIds());

        assertEquals(Integer.valueOf(oldTargetWeight), treeService.findOne(source.getId()).getWeight());
        assertEquals(Integer.valueOf(oldSourceWeight), treeService.findOne(target.getId()).getWeight());

    }

    @Test
    public void testMoveAsBeforeWithPrevSiblings2() {
        Tree t11 = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree target = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldTargetWeight = target.getWeight();

        Tree t12 = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldT12Weight = t12.getWeight();

        Tree source = createTree(root.getId(), root.makeSelfAsNewParentIds());
        int oldSourceWeight = source.getWeight();

        Tree t13 = createTree(root.getId(), root.makeSelfAsNewParentIds());

        treeService.move(source, target, "prev");

        clear();

        assertEquals(target.getParentId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.getParentId(), treeService.findOne(t12.getId()).getParentId());
        assertEquals(target.getParentIds(), treeService.findOne(source.getId()).getParentIds());

        assertEquals(Integer.valueOf(oldTargetWeight), treeService.findOne(source.getId()).getWeight());
        assertEquals(Integer.valueOf(oldSourceWeight), treeService.findOne(t12.getId()).getWeight());
        assertEquals(Integer.valueOf(oldT12Weight), treeService.findOne(target.getId()).getWeight());

    }

    @Test
    public void testMoveAsBeforeWithPrevSiblingsAndContains() {
        Tree newRoot = createTree(root.getId(), root.makeSelfAsNewParentIds());

        Tree t11 = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        Tree target = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        int oldTargetWeight = target.getWeight();
        Tree t12 = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        int oldT12Weight = t12.getWeight();

        Tree source = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree sourceChild = createTree(source.getId(), source.makeSelfAsNewParentIds());

        treeService.move(source, target, "prev");

        clear();

        assertEquals(target.getParentId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.getParentIds(), treeService.findOne(source.getId()).getParentIds());

        assertEquals(treeService.findOne(source.getId()).getId(), treeService.findOne(sourceChild.getId()).getParentId());
        assertEquals(treeService.findOne(source.getId()).makeSelfAsNewParentIds(), treeService.findOne(sourceChild.getId()).getParentIds());

        assertEquals(Integer.valueOf(oldTargetWeight), treeService.findOne(source.getId()).getWeight());
        assertEquals(Integer.valueOf(oldT12Weight), treeService.findOne(target.getId()).getWeight());
        assertEquals(Integer.valueOf(oldT12Weight + 1), treeService.findOne(t12.getId()).getWeight());

    }

    @Test
    public void testMoveAsAfterWithNoAfterSiblings() {
        Tree newRoot = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree t = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        Tree target = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());

        int oldTargetWeight = target.getWeight();
        Tree source = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        treeService.move(source, target, "next");

        clear();

        assertEquals(Integer.valueOf(oldTargetWeight + 1), treeService.findOne(source.getId()).getWeight());
        assertEquals(Integer.valueOf(oldTargetWeight), treeService.findOne(target.getId()).getWeight());

    }

    @Test
    public void testMoveAsAfterWithAfterSiblings() {
        Tree newRoot = createTree(root.getId(), root.makeSelfAsNewParentIds());
        Tree target = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        Tree t12 = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        int oldT12Weight = t12.getWeight();

        Tree source = createTree(newRoot.getId(), newRoot.makeSelfAsNewParentIds());
        treeService.move(source, target, "next");

        clear();

        assertEquals(target.getParentId(), treeService.findOne(source.getId()).getParentId());
        assertEquals(target.getParentIds(), treeService.findOne(source.getId()).getParentIds());

        assertEquals(Integer.valueOf(oldT12Weight), treeService.findOne(source.getId()).getWeight());
        assertEquals(Integer.valueOf(oldT12Weight + 1), treeService.findOne(t12.getId()).getWeight());

    }

    private Tree createTree(long parentId, String parentIds) {
        Tree tree = new Tree();
        tree.setParentId(parentId);
        tree.setParentIds(parentIds);
        treeService.save(tree);
        return tree;
    }

}
