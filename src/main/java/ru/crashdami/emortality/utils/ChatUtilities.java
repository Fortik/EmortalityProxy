package ru.crashdami.emortality.utils;

import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Session;
import ru.crashdami.emortality.EmortalityProxy;

public class ChatUtilities {

    public static String fixColor(String text) {
        return text.replace("&", "§").replace(">>", "»")
                .replace("$p", "§bEmortality§fProxy §7»§r")
                .replace("$vk", "https://vk.com/emortality")
                .replace("$ds", "https://dsc.gg/emortality")
                .replace("$ip", "proxy.emortality.xyz");
    }

    public static void broadcast(String text) {
        for (Session s : EmortalityProxy.getPlayers())
            s.send(new ServerChatPacket(fixColor(text)));
    }
}
