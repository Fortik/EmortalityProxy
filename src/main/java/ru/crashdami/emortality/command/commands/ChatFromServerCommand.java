package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class ChatFromServerCommand extends Command {

    public ChatFromServerCommand() {
        super("chatfromserver", "Wlacz/wylacz otrzymywanie wiadomosci od serwera!", ",chatfromserver",
                Group.PLAYER, "chatfromservers");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.playerOptions.chatFromServer) {
            p.sendMessage("$p &7Od teraz &cnie przyjmujesz&7 wiadomosci z &aserwera&7!");
            p.playerOptions.chatFromServer = false;
            return;
        }
        p.sendMessage("$p &7Od teraz &aprzyjmujesz&7 wiadomosci z &aserwera&7!");
        p.playerOptions.chatFromServer = true;
    }
}