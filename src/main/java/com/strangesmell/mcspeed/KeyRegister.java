package com.strangesmell.mcspeed;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE,value = Dist.CLIENT)
public class KeyRegister {
    public static final Lazy<KeyMapping> DAPEN_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.n2o", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_LEFT_CONTROL, // ctrl
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));

    public static final Lazy<KeyMapping> PIAOYI_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.piaoyi", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_CAPS_LOCK, // 大写
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));

    public static final Lazy<KeyMapping> XIAOPEN_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.xiaopen", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_SPACE, // 空格
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));
    public static final Lazy<KeyMapping> UP_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.up", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_UP, //
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));
    public static final Lazy<KeyMapping> DOWN_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.down", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_DOWN, //
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));
    public static final Lazy<KeyMapping> LEAFT_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.left", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_LEFT, //
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));
    public static final Lazy<KeyMapping> RIGHT_MAPPING = Lazy.of(() -> new KeyMapping(
            "key.mcspeed.right", // 将使用该翻译键进行本地化
            KeyConflictContext.IN_GAME, // 映射只能在当一个屏幕打开时使用
            InputConstants.Type.KEYSYM, // 在键盘上的默认映射
            GLFW.GLFW_KEY_RIGHT, //
            "key.categories.mcspeed" // 映射将在杂项（misc）类别中
    ));


/*    @SubscribeEvent
    public void registerBindings(RegisterKeyMappingsEvent event) {
        event.register(Ctrl_MAPPING.get());
    }*/

/*    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) { // 仅调用代码一次，因为tick事件在每个tick调用两次
            while (Ctrl_MAPPING.get().consumeClick()) {
                // 在此处执行单击时的逻辑
                assert Minecraft.getInstance().player != null;
                Entity speedBoat = Minecraft.getInstance().player.getControlledVehicle();
                if(speedBoat instanceof SpeedBoat){
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("你按下了ctrl"));
                }
            }
        }
    }*/
}
