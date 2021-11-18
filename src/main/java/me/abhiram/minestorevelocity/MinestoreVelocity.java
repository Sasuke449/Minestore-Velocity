package me.abhiram.minestorevelocity;

import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import me.abhiram.minestorevelocity.netty.NettyServerHandler;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(
        id = "minestorevelocity",
        name = "Minestore Velocity",
        version = "1.0-SNAPSHOT",
        description = "Official Minestore velocity plugin",
        url = "https://minestorecms.com",
        authors = {"abhiram"}
)
public class MinestoreVelocity {
    @Inject
    private Logger logger;

    @Inject
    private ProxyServer proxyServer;

    private static MinestoreVelocity instance;

    private final Toml config;

    @Inject
    public MinestoreVelocity(ProxyServer server, Logger logger, @DataDirectory final Path folder) {
        this.proxyServer = server;
        config = loadConfig(folder);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        instance = this;
        try {
            this.startNettyTcpServer(Integer.parseInt(getConfig().getString("port")));
        }catch (Exception exception){
            exception.printStackTrace();
            logger.info("There was an error while starting minestore");
        }
    }

    private void startNettyTcpServer(int port) throws Exception {
        logger.info("Starting Server at " + port);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(eventLoopGroup,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new NettyServerHandler())
                .childOption(ChannelOption.SO_KEEPALIVE,true);

        serverBootstrap.bind(port).sync();
    }

    private Toml loadConfig(Path path) {
        File folder = path.toFile();
        File file = new File(folder, "config.toml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStream input = getClass().getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }

        return new Toml().read(file);
    }

    public ProxyServer getProxyServer(){
        return this.proxyServer;
    }

    public Logger getLogger(){
        return this.logger;
    }

    public Toml getConfig(){
        return this.config;
    }

    public static MinestoreVelocity getInstance(){
        return instance;
    }
}
