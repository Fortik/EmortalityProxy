package ru.crashdami.emortality.command.commands.settings;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class AutoCaptchaCommand extends Command {

    public AutoCaptchaCommand() {
        super("autocaptcha", "Авто решение капчи в чате!", ",autocaptcha [bots/runnables]",
                Group.USER, "botsautocaptcha", "botautocaptcha");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("bots") || type.equalsIgnoreCase("bot")) {
            if (p.botOptions.autoCaptcha) {
                p.sendMessage("$p &7Автокапча для ботов &cвыключена&7!");
                p.botOptions.autoCaptcha = false;
                return;
            }
            p.sendMessage("$p &7Автокапча для ботов &aвключена&7!");
            p.botOptions.autoCaptcha = true;
        } else {
            if (p.playerOptions.autoCaptcha) {
                p.sendMessage("$p &7Автокапча для игрока&c выключена&7!");
                p.playerOptions.autoCaptcha = false;
                return;
            }
            p.sendMessage("$p &7Автокапча для игрока &aвключена&7!");
            p.playerOptions.autoCaptcha = true;
        }
    }
}