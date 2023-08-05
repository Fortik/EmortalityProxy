package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class BotQuitCommand extends Command {

    public BotQuitCommand() {
        super("botquit", "Боты покидают сервер!", ",botquit [all/nick bota]",
                Group.PLAYER, "botsquit", "bquit", "quitbot", "quitbots");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        if (p.getBots().size() < 1) {
            p.sendMessage("$p &cNie masz zadnych botow!");
            return;
        }
        final String nickBot = args[1];
        if (nickBot.equalsIgnoreCase("all")) {
            for (Bot bot : p.getBots())
                bot.getSession().disconnect("Rozlaczono za pomoca komendy");
        } else {
            boolean exists = false;
            for (Bot bot : p.getBots()) {
                if (bot.getName().equalsIgnoreCase(nickBot)) {
                    exists = true;
                    bot.getSession().disconnect("Rozlaczono za pomoca komendy");
                }
            }
            if (!exists)
                p.sendMessage("$p &cBot o nazwie: &7" + nickBot + "&c nie istnieje!");
        }
    }
}