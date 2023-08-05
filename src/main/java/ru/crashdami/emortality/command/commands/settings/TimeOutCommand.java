package ru.crashdami.emortality.command.commands.settings;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class TimeOutCommand extends Command {

    public TimeOutCommand() {
        super("timeout", "Установить таймаут ботам", ",timeout [runnables/bots] [ping/connect] [czas w sek]",
                Group.PLAYER, "connecttimeout", "settimeout");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 4) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        final String type2 = args[2];
        final Integer seconds = Integer.parseInt(args[3]);
        if (type.equalsIgnoreCase("runnables") || type.equalsIgnoreCase("PLAYER")) {
            if (type2.equalsIgnoreCase("ping")) {
                p.playerOptions.timeOutPing = seconds;
                p.sendMessage("$p &7Ustawiono timeout pingu na: &a" + p.playerOptions.timeOutPing);
            } else {
                p.playerOptions.timeOutConnect = seconds;
                p.sendMessage("$p &7Ustawiono timeout connecta na: &a" + p.playerOptions.timeOutConnect);
            }
        } else {
            if (type2.equalsIgnoreCase("ping")) {
                p.botOptions.timeOutPing = seconds;
                p.sendMessage("$p &7Ustawiono timeout pingu (botow) na: &a" + p.botOptions.timeOutPing);
            } else {
                p.botOptions.timeOutConnect = seconds;
                p.sendMessage("$p &7Ustawiono timeout connecta (botow) na: &a" + p.botOptions.timeOutConnect);
            }
        }
    }
}