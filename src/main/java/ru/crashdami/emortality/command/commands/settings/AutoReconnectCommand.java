package ru.crashdami.emortality.command.commands.settings;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class AutoReconnectCommand extends Command {

    public AutoReconnectCommand() {
        super("autoreconnect", "Авто переподключение на сервер!", ",autoreconnect [bots/runnables]",
                Group.USER, "botsautoreconnect", "botautoreconnect");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("bots") || type.equalsIgnoreCase("bot")) {
            if (p.botOptions.autoReconnect) {
                p.sendMessage("$p &7Авто-перезаход для ботов &cвыключен&7!");
                p.botOptions.autoReconnect = false;
                return;
            }
            p.sendMessage("$p &7Авто-перезаход для ботов &aвыключен&7!");
            p.botOptions.autoReconnect = true;
        } else {
            if (p.playerOptions.autoReconnect) {
                p.sendMessage("$p &7Авто-перезаход для игрока&c выключен&7!");
                p.playerOptions.autoReconnect = false;
                return;
            }
            p.sendMessage("$p &7Авто-перезаход для ботов &aвключен&7!");
            p.playerOptions.autoReconnect = true;
        }
    }
}