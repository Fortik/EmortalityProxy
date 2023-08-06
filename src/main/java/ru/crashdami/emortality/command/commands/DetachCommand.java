package ru.crashdami.emortality.command.commands;

import org.spacehq.mc.auth.data.GameProfile;
import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class DetachCommand extends Command {

    public DetachCommand() {
        super("detach", "Отключиться от бота", ",detach",
                Group.USER, "d", "ghost");
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
            p.setLastPacket("&c...");
            p.sendMessage("$p &aОтключен от сеанса бота! :)");
        } else {
            p.sendMessage("$p &cВы не подключены ни к одному серверу!");
        }
    }
}