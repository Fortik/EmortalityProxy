package ru.crashdami.emortality.utils;

import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.Session;
import ru.crashdami.emortality.EmortalityMain;

public class ChatUtilities {

    public static String fixColor(String text) {
        return text.replace("&", "§").replace(">>","»")
                .replace("$p", "§8[§bEmortality§fProxy§8]");
    }

    public static void broadcast(String text) {
        for (Session s : EmortalityMain.getPlayers())
            s.send(new ServerChatPacket(fixColor(text)));
    }
}
