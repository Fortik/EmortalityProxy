package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class QuitCommand extends Command {

    public QuitCommand() {
        super("quit", "Выйти с сервера!", ",quit",
                Group.USER, "wyjdz", "left", "leave", "q");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isConnected()) {
            p.getSessionConnect().disconnect("Отключен с помощью команды");
            p.setConnected(false);
            p.setSessionConnect(null);
            p.setLastPacketMs(0L);
            p.setLastPacketMs(0L);
            p.sendMessage("$p &aВы успешно отключены от сервера!");
        } else {
            p.sendMessage("$p &cВы не подключены ни к одному серверу!");
        }
    }
}