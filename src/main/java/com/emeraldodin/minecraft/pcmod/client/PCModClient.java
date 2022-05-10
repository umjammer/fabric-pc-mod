package com.emeraldodin.minecraft.pcmod.client;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.emeraldodin.minecraft.pcmod.client.entities.render.FlatScreenRender;
import com.emeraldodin.minecraft.pcmod.client.entities.render.ItemPreviewRender;
import com.emeraldodin.minecraft.pcmod.client.gui.FocusPCScreen;
import com.emeraldodin.minecraft.pcmod.entities.ItemPreviewEntity;
import com.emeraldodin.minecraft.pcmod.entities.EntityList;
import com.emeraldodin.minecraft.pcmod.main.PCMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.SystemUtils;
import org.lwjgl.glfw.GLFW;

import static com.emeraldodin.minecraft.pcmod.main.PCMod.logger;

public class PCModClient implements ClientModInitializer {
    public static ItemPreviewEntity thePreviewEntity;
    public static Map<UUID, Identifier> vmScreenTextures;

    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey1;
    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey2;
    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey3;
    @SuppressWarnings("SpellCheckingInspection")
    public static int glfwUnfocusKey4;

    public static boolean releaseKeys = false;
    public static double mouseLastX = 0;
    public static double mouseLastY = 0;
    public static double mouseCurX = 0;
    public static double mouseCurY = 0;
    public static int mouseDeltaScroll;
    public static boolean leftMouseButton;
    public static boolean middleMouseButton;
    public static boolean rightMouseButton;
    public static boolean isOnClient = false;

    public static VNCReceiver vnc;

    static {
        // TODO properties
        if (SystemUtils.IS_OS_MAC) {
            logger.info("on mac");
            glfwUnfocusKey1 = GLFW.GLFW_KEY_LEFT_CONTROL;
            glfwUnfocusKey2 = GLFW.GLFW_KEY_LEFT_ALT;
            glfwUnfocusKey3 = GLFW.GLFW_KEY_DELETE;
        } else {
            logger.info("on windows");
            glfwUnfocusKey1 = GLFW.GLFW_KEY_LEFT_CONTROL;
            glfwUnfocusKey2 = GLFW.GLFW_KEY_RIGHT_CONTROL;
            glfwUnfocusKey3 = GLFW.GLFW_KEY_BACKSPACE;
        }
        glfwUnfocusKey4 = -1;
    }

    public static void openPCFocusGUI() {
        MinecraftClient.getInstance().setScreen(new FocusPCScreen());
    }

    public static String getKeyName(int key) {
        if (key < 0) {
            return "None";
        } else {
            return glfwKey(key);
        }
    }

    private static String glfwKey(int key) {
        switch (key) {
        case GLFW.GLFW_KEY_LEFT_CONTROL:
            return "L Control";
        case GLFW.GLFW_KEY_RIGHT_CONTROL:
            return "R Control";
        case GLFW.GLFW_KEY_RIGHT_ALT:
            return "R Alt";
        case GLFW.GLFW_KEY_LEFT_ALT:
            return "L Alt";
        case GLFW.GLFW_KEY_LEFT_SHIFT:
            return "L Shift";
        case GLFW.GLFW_KEY_RIGHT_SHIFT:
            return "R Shift";
        case GLFW.GLFW_KEY_ENTER:
            return "Enter";
        case GLFW.GLFW_KEY_ESCAPE:
            return "Escape";
        case GLFW.GLFW_KEY_BACKSPACE:
            return "Backspace";
        case GLFW.GLFW_KEY_DELETE:
            return "Delete";
        case GLFW.GLFW_KEY_CAPS_LOCK:
            return "Caps Lock";
        case GLFW.GLFW_KEY_TAB:
            return "Tab";
        default:
            return GLFW.glfwGetKeyName(key, 0);
        }
    }

    @Override
    public void onInitializeClient() {
        isOnClient = true;
        vmScreenTextures = new HashMap<>();

        EntityRendererRegistry.register(EntityList.FLATSCREEN, FlatScreenRender::new);

        EntityRendererRegistry.register(EntityList.ITEM_PREVIEW, ItemPreviewRender::new);

        // vnc
        logger.info("vnc start connecting");
        vnc = new VNCReceiver(PCMod.host, PCMod.port);
        vnc.connect();
        logger.info("vnc connected: " + vnc.client.isRunning());
    }
}
