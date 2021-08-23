package me.kodysimpson.understandingnms;


import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class Listener implements org.bukkit.event.Listener {

    //MADE WITH SPIGOT MAPPINGS
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Player p = e.getPlayer(); //API
        CraftPlayer craftPlayer = (CraftPlayer) p; //CraftBukkit
        EntityPlayer entityPlayer = craftPlayer.getHandle(); //NMS

        //To send a packet with NMS, a PlayerConnection needs to be obtained from the EntityPlayer
        //As you can see, the field name(in this case "b") is obscure and hard to understand its purpose.
        //This means you need to read the datatypes as well as example code and documentation.
        PlayerConnection playerConnection = entityPlayer.b;

        //A packet that can be used to effect a players game state is seen here. I learned how to use it by looking at the
        //entityPlayer.setPlayerWeather() source code as well as https://wiki.vg/Protocol
        //Each a-l static member represents a game state, you can use these to set the first field for the packet
        //For field C AKA 2 on the wiki, it doesn't require a "value" so it's set to 0 as a dummy value.
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.c, 0.0f);
        playerConnection.sendPacket(packet);
        //craftPlayer.setPlayerWeather(WeatherType.CLEAR); <--- does the same thing

        //Sending a message using NMS
        entityPlayer.sendMessage(IChatBaseComponent.a("Sending weather change"), entityPlayer.getUniqueID());

    }

    //THIS IS THE MOJANG MAPPING EQUIVALENT
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){

        Player p = e.getPlayer();
        CraftPlayer craftPlayer = (CraftPlayer) p;
        ServerPlayer serverPlayer = craftPlayer.getHandle();

        ServerGamePacketListenerImpl listener = serverPlayer.connection;

        ClientboundGameEventPacket clientboundGameEventPacket = new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0f);
        listener.send(clientboundGameEventPacket);

        serverPlayer.sendMessage(Component.nullToEmpty("Sending weather change"), serverPlayer.getUUID());

    }

}
