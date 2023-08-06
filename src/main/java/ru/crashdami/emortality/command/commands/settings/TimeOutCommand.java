package ru.crashdami.emortality.command.commands.settings;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class TimeOutCommand extends Command {

    public TimeOutCommand() {
        super("timeout", "Установить время ожидания пинга/входа!", ",timeout [runnables/bots] [ping/connect] [задержка]",
                Group.USER, "connecttimeout", "settimeout");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 4) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        final String type = args[1];
        final String type2 = args[2];
        final Integer seconds = Integer.parseInt(args[3]);
        if (type.equalsIgnoreCase("runnables") || type.equalsIgnoreCase("gracz")) {
            if (type2.equalsIgnoreCase("ping")) {
                p.playerOptions.timeOutPing = seconds;
                p.sendMessage("$p &7Установлено время ожидание пинга: &b" + p.playerOptions.timeOutPing);
            } else {
                p.playerOptions.timeOutConnect = seconds;
                p.sendMessage("$p &7Установлено время ожидание входа: &b" + p.playerOptions.timeOutPing);
            }
        } else {
            if (type2.equalsIgnoreCase("ping")) {
                p.botOptions.timeOutPing = seconds;
                p.sendMessage("$p &7Установлено время ожидание пинга: &b" + p.botOptions.timeOutPing);
            } else {
                p.botOptions.timeOutConnect = seconds;
                p.sendMessage("$p &7Установлено время ожидание входа: &b" + p.botOptions.timeOutConnect);
            }
        }
    }
}