package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class BotListCommand extends Command {

    public BotListCommand() {
        super("botlist", "Список запущеных ботов!", ",botlist",
                Group.USER, "listbot", "listabotow");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.getBots().size() < 1) {
            p.sendMessage("$p &cВы еще не запустили не одного бота!");
            return;
        }
        p.sendMessage("&7Количество ботов: &a" + p.getBots().size());
        for (int i = 0; i < p.getBots().size(); i++) {
            final Bot bot = p.getBots().get(i);
            p.sendMessage("&2" + i + 1 + ". &7Никнейм: &b" + bot.getName() + "&7, сервер: &b" + bot.getSession().getHost());
        }
    }
}