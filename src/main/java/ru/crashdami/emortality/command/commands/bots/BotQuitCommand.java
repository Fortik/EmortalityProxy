package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class BotQuitCommand extends Command {

    public BotQuitCommand() {
        super("botquit", "Выход ботов с сервера!", ",botquit [all/ник бота]",
                Group.USER, "botsquit", "bquit", "quitbot", "quitbots");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &a" + getUsage());
            return;
        }
        if (p.getBots().size() < 1) {
            p.sendMessage("$p &cВы еще не запустили не одного бота!");
            return;
        }
        final String nickBot = args[1];
        if (nickBot.equalsIgnoreCase("all")) {
            for (Bot bot : p.getBots())
                bot.getSession().disconnect("Отключен по команде");
        } else {
            boolean exists = false;
            for (Bot bot : p.getBots()) {
                if (bot.getName().equalsIgnoreCase(nickBot)) {
                    exists = true;
                    bot.getSession().disconnect("Отключен по команде");
                }
            }
            if (!exists)
                p.sendMessage("$p &cБот с ником: &7" + nickBot + "&c не найден!");
        }
    }
}