package me.abhiram.minestorevelocity.netty;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import me.abhiram.minestorevelocity.MinestoreVelocity;
import me.abhiram.minestorevelocity.model.MinestoreCommand;

public class NettyMinestoreCommandHandler extends SimpleChannelInboundHandler<String> {
    private final Gson gson = new Gson();
    private static final MinecraftChannelIdentifier MODERN_BUNGEE_CHANNEL = MinecraftChannelIdentifier.create("my", "minestore");


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String incoming) throws Exception {
        MinestoreCommand command = gson.fromJson(incoming, MinestoreCommand.class);

        String password = MinestoreVelocity.getInstance().getConfig().getString("websocket-password");

        ByteArrayDataOutput sendcontent = ByteStreams.newDataOutput();
        sendcontent.writeUTF(command.getCommand());

        if(command.getPassword().equalsIgnoreCase(password)) {
            for (RegisteredServer server : MinestoreVelocity.getInstance().getProxyServer().getAllServers()) {
                server.sendPluginMessage(MODERN_BUNGEE_CHANNEL,sendcontent.toByteArray());
            }
            MinestoreVelocity.getInstance().getLogger().info("Send Order To Minestore-Spigot/Bukkit plugin!");
        }else {
            MinestoreVelocity.getInstance().getLogger().info("Unable to send order to Minestore-Spigot/Bukkit plugin.(ERROR: Invalid password)");
        }
    }
}
