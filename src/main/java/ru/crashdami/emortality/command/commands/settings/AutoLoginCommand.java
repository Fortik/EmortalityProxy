package ru.crashdami.emortality.command.commands.settings;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class AutoLoginCommand extends Command {

    public AutoLoginCommand() {
        super("autologin", "Автоматическая регистрация!", ",autologin [bots/runnables]",
                Group.USER, "botsautologin", "autoregister");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("bots") || type.equalsIgnoreCase("bot")) {
            if (p.botOptions.autoLogin) {
                p.sendMessage("$p &7Автологин для ботов &cвыключен&7!");
                p.botOptions.autoLogin = false;
                return;
            }
            p.sendMessage("$p &7Автологин для ботов &aвключен&7!");
            p.botOptions.autoLogin = true;
        } else {
            if (p.playerOptions.autoLogin) {
                p.sendMessage("$p &7Автологин для игрока&c выключен&7!");
                p.playerOptions.autoLogin = false;
                return;
            }
            p.sendMessage("$p &7Автологин для игрока &aвключен&7!");
            p.playerOptions.autoLogin = true;
        }
    }
}