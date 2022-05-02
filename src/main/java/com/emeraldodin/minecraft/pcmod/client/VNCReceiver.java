package com.emeraldodin.minecraft.pcmod.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.io.EOFException;
import java.net.SocketException;

import com.emeraldodin.minecraft.pcmod.main.PCMod;
import com.shinyhut.vernacular.client.VernacularClient;
import com.shinyhut.vernacular.client.VernacularConfig;
import com.shinyhut.vernacular.client.rendering.ColorDepth;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;

import static com.emeraldodin.minecraft.pcmod.main.PCMod.logger;


public class VNCReceiver {

    private String host;
    private int port;

    private int width = 320, height = 320;

    public BufferedImage image;
    private Graphics g;
    ColorModel colorModel;
    Raster raster;

    private NativeImage ni;
    private NativeImageBackedTexture lastNIBT;

    public static VNCReceiver current;

    public VernacularClient client;

    private int connectionTrial = 0;

    public VNCReceiver(String host, int port) {
        this.host = host;
        this.port = port;
        this.current = this;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.colorModel = image.getColorModel();
        this.raster = image.getRaster();
        this.g = image.createGraphics();
        // We need to use ABGR otherwise we can't set a custom pixel color
        this.ni = new NativeImage(NativeImage.Format.RGBA, width, height, true);
logger.info("image: " + width + ", " + height);
    }

    public void connect() {
        MinecraftClient mcc = MinecraftClient.getInstance();
        VernacularConfig config = new VernacularConfig();
        this.client = new VernacularClient(config);

//        config.setUsernameSupplier(PCMod::getUsername);
        config.setPasswordSupplier(PCMod::getPassword);

        // Select 8-bits per pixel indexed color, or 8/16/24 bits per pixel true color
        // mac server doesn't accept 8_index ???
        // TODO property
        config.setColorDepth(ColorDepth.BPP_16_TRUE);

        // Set up callbacks for the various events that can happen in a VNC session

        // Exception handler
        config.setErrorListener(e -> {
            if (e.getCause() instanceof EOFException || e.getCause() instanceof SocketException) {
                if (connectionTrial < 3) {
                    connectionTrial++;
                    logger.info("connection trial: " + connectionTrial);
                    if (client.isRunning())
                        client.stop();
                    client.start(host, port);
                    return;
                }
            }
            e.printStackTrace();
        });

        // Handle system bell events from the remote host
        config.setBellListener(v -> System.out.println("DING!"));

        config.setScreenUpdateListener(i -> {
            g.drawImage(i, 0, 0, width, height, 0, 0, i.getWidth(null), i.getHeight(null), null);
//logger.info("paint: " + i.getWidth(null) + ", " + i.getHeight(null));
            if (mcc.player == null) return;
            try {
                if (!PCModClient.vmScreenTextures.containsKey(mcc.player.getUuid())) {
                    @SuppressWarnings("SpellCheckingInspection")
                    NativeImageBackedTexture nibt = createNIBT();
                    lastNIBT = nibt;
                    PCModClient.vmScreenTextures.put(mcc.player.getUuid(), mcc.getTextureManager().registerDynamicTexture("pc_screen_mp", nibt));
                } else {
                    drawNI();
                    lastNIBT.upload();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        while (retry < 3) {
            try {
                client.start(host, port);
logger.info("vnc started : " + retry);
                break;
            } catch (IllegalArgumentException e) {
                logger.info("error: " + e);
                retry++;
                if (client.isRunning())
                    client.stop();
            }
        }
    }

    int retry;

    private void drawNI() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Object elements = raster.getDataElements(x, y, (Object) null);

                int red = colorModel.getRed(elements);
                int green = colorModel.getGreen(elements);
                int blue = colorModel.getBlue(elements);
                int out = 255 << 24 | blue << 16 | green << 8 | red;

                ni.setColor(x, y, out);
            }
        }
    }

    private NativeImageBackedTexture createNIBT() {
        drawNI();
        return new NativeImageBackedTexture(ni);
    }
}
