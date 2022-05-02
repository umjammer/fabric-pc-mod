package com.emeraldodin.minecraft.pcmod.client.utils;

import com.emeraldodin.minecraft.pcmod.client.VNCReceiver;
import com.emeraldodin.minecraft.pcmod.client.gui.PCScreenFocus;

import net.minecraft.client.MinecraftClient;

import static com.emeraldodin.minecraft.pcmod.client.PCModClient.leftMouseButton;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseCurY;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastX;
import static com.emeraldodin.minecraft.pcmod.client.PCModClient.mouseLastY;

public class VNCControlRunnable implements Runnable {

    private boolean lastLeftMouseButton = false;

    @Override
    public void run() {
        MinecraftClient mcc = MinecraftClient.getInstance();

        while (true) {
            try {
                Thread.sleep(33);
                Thread.yield();

                double deltaX = 0;
                double deltaY = 0;

                deltaX = mouseCurX - mouseLastX;
                deltaY = mouseCurY - mouseLastY;
                mouseLastX = mouseCurX;
                mouseLastY = mouseCurY;

                if (mcc.currentScreen instanceof PCScreenFocus) {
                    if ((Math.abs(deltaX) + Math.abs(deltaY)) > 2) {
                        VNCReceiver.current.client.moveMouse((int) mouseCurX, (int) mouseCurY);
                    }

                    if (lastLeftMouseButton != leftMouseButton) {
                        VNCReceiver.current.client.updateMouseButton(1, leftMouseButton);
                        lastLeftMouseButton = leftMouseButton;
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }
}
