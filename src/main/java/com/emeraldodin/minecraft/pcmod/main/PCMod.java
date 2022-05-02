package com.emeraldodin.minecraft.pcmod.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.emeraldodin.minecraft.pcmod.entities.EntityList;
import com.emeraldodin.minecraft.pcmod.item.ItemList;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PCMod implements ModInitializer {

	public static final String MODID = "pcmod";

	public static Logger logger = LogManager.getLogger(MODID);

	// vnc properties
	public static String host;
	public static int port;
	private static String username;
	private static String password;

	@Override
	public void onInitialize() {
		ItemList.init();
		EntityList.init();

		// vnc properties
		try {
			Path config = FabricLoader.getInstance().getConfigDir().resolve(MODID).resolve("config.properties");
			Properties props = new Properties();
			props.load(Files.newBufferedReader(config));
			host = props.getProperty("host", "127.0.0.1");
			port = Integer.parseInt(props.getProperty("port", "5900"));
			username = props.getProperty("username");
			password = props.getProperty("password");
logger.info("host: " + host + ", port: " + port + ", username: " + username);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	// vnc properties
	public static String getUsername() {
		return username;
	}

	// vnc properties
	public static String getPassword() {
		return password;
	}
}
