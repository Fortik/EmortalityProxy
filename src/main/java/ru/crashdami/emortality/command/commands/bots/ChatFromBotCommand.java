package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class ChatFromBotCommand extends Command {

    public ChatFromBotCommand() {
        super("chatfrombots", "Видеть сообщения ботов!", ",chatfrombots",
                Group.USER, "chatfrombot");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.playerOptions.chatFromBots) {
            p.sendMessage("$p &7Сообщения от ботов &cвыключены&7!");
            p.playerOptions.chatFromBots = false;
            return;
        }
        p.sendMessage("$p &7Сообщения от ботов &aвключены&7!");
        p.playerOptions.chatFromBots = true;
    }
}