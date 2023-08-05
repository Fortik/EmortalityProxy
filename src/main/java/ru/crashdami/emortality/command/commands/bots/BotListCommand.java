package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class BotListCommand extends Command {

    public BotListCommand() {
        super("botlist", "Boty pisza na czacie!", ",botlist",
                Group.PLAYER, "listbot", "listabotow");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.getBots().size() < 1) {
            p.sendMessage("$p &cNie masz zadnych botow!");
            return;
        }
        p.sendMessage("&7Ilosc botow: &a" + p.getBots().size());
        for (int i = 0; i < p.getBots().size(); i++) {
            final Bot bot = p.getBots().get(i);
            p.sendMessage("&2" + i + 1 + ". &7nick: &a" + bot.getName() + "&7, serwer: &a" + bot.getSession().getHost());
        }
    }
}