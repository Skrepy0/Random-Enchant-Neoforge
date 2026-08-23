package com.random_enchant.mixin_helper;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * 通过反射调用Curios API，避免直接依赖。
 * 如果Curios未安装，所有方法返回{@link Optional#empty()}或-1。
 */
public final class CuriosApiHelper {

    private static final Method GET_CURIOS_INVENTORY = resolveGetCuriosInventory();
    private static final Method SET_EQUIPPED_CURIO = resolveSetEquippedCurio();
    private static final Method GET_CURIOS = resolveGetCurios();

    private CuriosApiHelper() {}

    private static Method resolveGetCuriosInventory() {
        if (!CuriosHelper.isCuriosLoaded()) return null;
        try {
            Class<?> api = Class.forName("top.theillusivec4.curios.api.CuriosApi");
            return api.getMethod("getCuriosInventory", LivingEntity.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method resolveSetEquippedCurio() {
        if (!CuriosHelper.isCuriosLoaded()) return null;
        try {
            Class<?> handler = Class.forName("top.theillusivec4.curios.api.type.inventory.ICuriosItemHandler");
            return handler.getMethod("setEquippedCurio", String.class, int.class, ItemStack.class);
        } catch (Exception e) {
            return null;
        }
    }

    private static Method resolveGetCurios() {
        if (!CuriosHelper.isCuriosLoaded()) return null;
        try {
            Class<?> handler = Class.forName("top.theillusivec4.curios.api.type.inventory.ICuriosItemHandler");
            return handler.getMethod("getCurios");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取实体指定curio槽位中的第一个匹配物品。
     *
     * @param entity 实体
     * @param slotId 槽位ID（如"back"、"feet"）
     * @return 槽位中的第一个非空物品，如果Curios不存在或槽位不存在则返回空
     */
    public static Optional<ItemStack> getFirstStackInSlot(LivingEntity entity, String slotId) {
        if (GET_CURIOS_INVENTORY == null) return Optional.empty();
        try {
            Object result = GET_CURIOS_INVENTORY.invoke(null, entity);
            // getCuriosInventory returns Optional<ICuriosItemHandler>, need to unwrap
            if (result instanceof Optional<?> opt) {
                if (opt.isEmpty()) return Optional.empty();
                Object handler = opt.get();
                return getStackFromHandler(handler, slotId);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 检查实体指定curio槽位中是否存在指定条件的物品（使用{@link java.util.function.Predicate}模拟）。
     *
     * @param entity    实体
     * @param slotId    槽位ID
     * @param predicate 检查函数（参数为ItemStack，返回boolean）
     * @return 如果存在匹配物品返回true，否则false
     */
    public static boolean hasStackInSlot(LivingEntity entity, String slotId, Predicate<ItemStack> predicate) {
        if (GET_CURIOS_INVENTORY == null) return false;
        try {
            Object result = GET_CURIOS_INVENTORY.invoke(null, entity);
            // getCuriosInventory returns Optional<ICuriosItemHandler>, need to unwrap
            if (result instanceof Optional<?> opt) {
                if (opt.isEmpty()) return false;
                Object handler = opt.get();
                return checkSlot(handler, slotId, predicate);
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置指定Curios槽位中的物品。
     *
     * @param entity 实体
     * @param slotId 槽位ID（如"back"、"charm"）
     * @param index 槽位内的索引
     * @param stack 要设置的物品堆栈
     */
    public static void setEquippedCurio(LivingEntity entity, String slotId, int index, ItemStack stack) {
        if (SET_EQUIPPED_CURIO == null || GET_CURIOS_INVENTORY == null) return;
        try {
            Object result = GET_CURIOS_INVENTORY.invoke(null, entity);
            if (result instanceof Optional<?> opt && opt.isPresent()) {
                Object handler = opt.get();
                SET_EQUIPPED_CURIO.invoke(handler, slotId, index, stack);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * 查找第一个匹配条件的Curios槽位信息。
     *
     * @param entity 实体
     * @param predicate 匹配条件
     * @return 槽位信息，如果未找到则返回null
     */
    public static CurioSlotInfo findFirstCurioSlot(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (GET_CURIOS_INVENTORY == null || GET_CURIOS == null) return null;
        try {
            Object result = GET_CURIOS_INVENTORY.invoke(null, entity);
            if (!(result instanceof Optional<?> opt) || opt.isEmpty()) return null;
            Object handler = opt.get();

            Object curiosMap = GET_CURIOS.invoke(handler);
            if (!(curiosMap instanceof Map<?, ?> curios)) return null;

            for (Map.Entry<?, ?> entry: curios.entrySet()) {
                String slotId = (String) entry.getKey();
                Object stackHandler = entry.getValue();
                Method getSlots = stackHandler.getClass().getMethod("getSlots");
                int slots = (int) getSlots.invoke(stackHandler);
                Method getStackInSlot = stackHandler.getClass().getMethod("getStackInSlot", int.class);
                for (int i = 0; i < slots; i++) {
                    ItemStack stack = (ItemStack) getStackInSlot.invoke(stackHandler, i);
                    if (!stack.isEmpty() && predicate.test(stack)) {
                        return new CurioSlotInfo(slotId, i, stack.copy());
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    // ===== 内部反射方法 =====

    @SuppressWarnings("unchecked")
    private static Optional<ItemStack> getStackFromHandler(Object handler, String slotId) throws Exception {
        Method getStacksHandler = handler.getClass().getMethod("getStacksHandler", String.class);
        Object stacksHandlerOpt = getStacksHandler.invoke(handler, slotId);
        if (stacksHandlerOpt instanceof Optional<?> opt && opt.isPresent()) {
            Object stacksHandler = opt.get();
            Method getStacks = stacksHandler.getClass().getMethod("getStacks");
            Object dynamicHandler = getStacks.invoke(stacksHandler);
            Method getSlots = dynamicHandler.getClass().getMethod("getSlots");
            int slots = (int) getSlots.invoke(dynamicHandler);
            for (int i = 0; i < slots; i++) {
                Method getStackInSlot = dynamicHandler.getClass().getMethod("getStackInSlot", int.class);
                ItemStack stack = (ItemStack) getStackInSlot.invoke(dynamicHandler, i);
                if (!stack.isEmpty()) return Optional.of(stack);
            }
        }
        return Optional.empty();
    }

    private static boolean checkSlot(Object handler, String slotId, Predicate<ItemStack> predicate) throws Exception {
        Method getStacksHandler = handler.getClass().getMethod("getStacksHandler", String.class);
        Object stacksHandlerOpt = getStacksHandler.invoke(handler, slotId);
        if (stacksHandlerOpt instanceof Optional<?> opt && opt.isPresent()) {
            Object stacksHandler = opt.get();
            Method getStacks = stacksHandler.getClass().getMethod("getStacks");
            Object dynamicHandler = getStacks.invoke(stacksHandler);
            Method getSlots = dynamicHandler.getClass().getMethod("getSlots");
            int slots = (int) getSlots.invoke(dynamicHandler);
            for (int i = 0; i < slots; i++) {
                Method getStackInSlot = dynamicHandler.getClass().getMethod("getStackInSlot", int.class);
                ItemStack stack = (ItemStack) getStackInSlot.invoke(dynamicHandler, i);
                if (!stack.isEmpty() && predicate.test(stack)) return true;
            }
        }
        return false;
    }

    /**
     * Curios槽位信息记录。
     */
    public static final class CurioSlotInfo {
        private final String slotId;
        private final int index;
        private final ItemStack stack;

        public CurioSlotInfo(String slotId, int index, ItemStack stack) {
            this.slotId = slotId;
            this.index = index;
            this.stack = stack;
        }

        public String slotId() { return slotId; }
        public int index() { return index; }
        public ItemStack stack() { return stack; }
    }
}
