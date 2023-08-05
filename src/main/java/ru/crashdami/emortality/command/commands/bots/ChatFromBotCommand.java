package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class ChatFromBotCommand extends Command {

    public ChatFromBotCommand() {
        super("chatfrombots", "Otrzymuj wiadomosci z chatu botow!", ",chatfrombots",
                Group.PLAYER, "chatfrombot");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.playerOptions.chatFromBots) {
            p.sendMessage("$p &7Od teraz &cnie przyjmujesz&7 wiadomosci z &achatu botow&7!");
            p.playerOptions.chatFromBots = false;
            return;
        }
        p.sendMessage("$p &7Od teraz &aprzyjmujesz&7 wiadomosci z &achatu botow&7!");
        p.playerOptions.chatFromBots = true;
    }
}