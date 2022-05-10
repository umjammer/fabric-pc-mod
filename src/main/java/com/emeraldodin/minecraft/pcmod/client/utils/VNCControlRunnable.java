package com.emeraldodin.minecraft.pcmod.client.utils;

import com.emeraldodin.minecraft.pcmod.client.PCModClient;
import com.emeraldodin.minecraft.pcmod.client.VNCReceiver;
import net.minecraft.client.MinecraftClient;

import static com.emeraldodin.minecraft.pcmod.client.PCModClient.leftMouseButton;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurY;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastY;

public class VNCControlRunnable {

    private static boolean lastLeftMouseButton = false;

    public static void tick(MinecraftClient mcc) {
        try {
            double deltaX = 0;
            double deltaY = 0;

            deltaX = mouseCurX - mouseLastX;
            deltaY = mouseCurY - mouseLastY;
            mouseLastX = mouseCurX;
            mouseLastY = mouseCurY;

            if ((Math.abs(deltaX) + Math.abs(deltaY)) > 2) {
//logger.info(String.format("mouse: %d, %d", (int) mouseCurX, (int) mouseCurY));
                PCModClient.vnc.client.moveMouse((int) mouseCurX, (int) mouseCurY);
            }

            if (lastLeftMouseButton != leftMouseButton) {
                PCModClient.vnc.client.updateMouseButton(1, leftMouseButton);
                lastLeftMouseButton = leftMouseButton;
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}
