package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class QuitCommand extends Command {

    public QuitCommand() {
        super("quit", "Выход из сервера (полное отключение)", ",quit",
                Group.PLAYER, "wyjdz", "left", "leave", "q");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isConnected()) {
            p.getSessionConnect().disconnect("Rozlaczono przy uzyciu komendy");
            p.setConnected(false);
            p.setSessionConnect(null);
            p.setLastPacketMs(0L);
            p.setLastPacketMs(0L);
            p.sendMessage("$p &aRozlaczono przy uzyciu komendy! :)");
        } else {
            p.sendMessage("$p &cNie jestes polaczony z zadnym serwerem!");
        }
    }
}