package ru.crashdami.emortality.command.commands;

import org.spacehq.mc.auth.data.GameProfile;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class DetachCommand extends Command {

    public DetachCommand() {
        super("detach", "Rozlacz sie od bota, serwer pozostaje na serwrze", ",detach",
                Group.PLAYER, "d", "ghost");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isConnected() && p.getSessionConnect() != null) {
            final GameProfile profile = p.getSessionConnect().getFlag("profile");
            final Bot bot = new Bot(p, profile.getName(), p.getSessionConnect());
            p.addBot(bot);
            p.getSessionConnect().getListeners().forEach(l -> p.getSessionConnect().removeListener(l));
            p.setConnected(false);
            p.setSessionConnect(null);
            p.setLastPacketMs(0L);
            p.setLastPacket("&cRozlaczono");
            p.sendMessage("$p &aOdlaczono od sesji bota! :)");
        } else {
            p.sendMessage("$p &cNie jestes polaczony z zadnym serwerem!");
        }
    }
}