package net.t0ky.monojukebox;

import net.fabricmc.loader.api.FabricLoader;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class JukeboxConfig {
    public static double blendFactor = 0.70;
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("jukebox_mono.properties");

    public static void load() {
        if (!Files.exists(CONFIG_PATH)) {
            save();
            return;
        }
        try (InputStream in = Files.newInputStream(CONFIG_PATH)) {
            Properties props = new Properties();
            props.load(in);
            blendFactor = Double.parseDouble(props.getProperty("blendFactor", "0.70"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save() {
        try (OutputStream out = Files.newOutputStream(CONFIG_PATH)) {
            Properties props = new Properties();
            props.setProperty("blendFactor", String.valueOf(blendFactor));
            props.store(out, "Jukebox Mono Configuration");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
