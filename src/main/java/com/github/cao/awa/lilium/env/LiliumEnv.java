package com.github.cao.awa.lilium.env;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.lilium.await.AwaitManager;
import com.github.cao.awa.lilium.client.LiliumClient;
import com.github.cao.awa.lilium.framework.config.ConfigFramework;
import com.github.cao.awa.lilium.framework.event.EventFramework;
import com.github.cao.awa.lilium.framework.network.event.NetworkEventFramework;
import com.github.cao.awa.lilium.framework.network.packet.PacketFramework;
import com.github.cao.awa.lilium.framework.plugin.PluginFramework;
import com.github.cao.awa.lilium.framework.serialize.BinarySerializeFramework;
import com.github.cao.awa.lilium.network.io.client.LiliumClientNetworkIo;
import com.github.cao.awa.lilium.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.lilium.plugin.internal.mod.network.packet.inbound.update.UpdateModInformationPacket;
import com.github.cao.awa.lilium.server.LiliumServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LiliumEnv {
    private static final Logger LOGGER = LogManager.getLogger("LiliumEnv");
    public static LiliumServer SERVER;
    public static final AwaitManager awaitManager = new AwaitManager();
    public static final ConfigFramework CONFIG_FRAMEWORK = new ConfigFramework();
    public static final BinarySerializeFramework BINARY_SERIALIZE_FRAMEWORK = new BinarySerializeFramework();
    public static final PacketFramework PACKET_FRAMEWORK = new PacketFramework();
    public static final PluginFramework PLUGIN_FRAMEWORK = new PluginFramework();
    public static final EventFramework EVENT_FRAMEWORK = new EventFramework();
    public static final NetworkEventFramework NETWORK_EVENT_FRAMEWORK = new NetworkEventFramework();

    private static void bootstrapFrameworks() {
        CONFIG_FRAMEWORK.work();
        BINARY_SERIALIZE_FRAMEWORK.work();
        PACKET_FRAMEWORK.work();
        PLUGIN_FRAMEWORK.work();
        EVENT_FRAMEWORK.work();
        NETWORK_EVENT_FRAMEWORK.work();
    }

    public static void bootstrapServer() {
        bootstrapFrameworks();

        SERVER = new LiliumServer();

        try {
            SERVER.bootstrap();
        } catch (Exception e) {
            LOGGER.error("Failed to bootstrap the server", e);
        }
    }

    public static void bootstrapClient() {
        bootstrapFrameworks();

        LiliumClient client = new LiliumClient(router -> {
            UpdateModInformationPacket packet = new UpdateModInformationPacket("Xxx mod");
            router.send(packet);
        });

        try {
            new LiliumClientNetworkIo(client).connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        bootstrapClient();
    }
}
