package ru.crashdami.emortality.command.commands.settings;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class AutoReconnectTimeCommand extends Command {

    public AutoReconnectTimeCommand() {
        super("autoreconnecttime", "Установка своего времени для авто реджоина", ",autoreconnecttime [bots/runnables] [задержка]",
                Group.USER, "botsautoreconnecttime", "botautoreconnecttime");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 3) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        final String type = args[1];
        final Integer sec = Integer.parseInt(args[2]);
        if (type.equalsIgnoreCase("bots") || type.equalsIgnoreCase("bot")) {
            p.botOptions.autoReconnectTime = sec;
            p.sendMessage("$p &7Успешно установлено: &b" + sec);
        } else {
            p.playerOptions.autoReconnectTime = sec;
            p.sendMessage("$p &7Успешно установлено: &b" + sec);
        }
    }
}