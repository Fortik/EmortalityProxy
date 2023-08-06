package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.objects.Player;

public class ChatFromServerCommand extends Command {

    public ChatFromServerCommand() {
        super("chatfromserver", "Включить/выключить получение сообщений с сервера!", ",chatfromserver",
                Group.USER, "chatfromservers");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.playerOptions.chatFromServer) {
            p.sendMessage("$p &7Больше &cвы не получаете&7 сообщений от &aсервера&7!");
            p.playerOptions.chatFromServer = false;
            return;
        }
        p.sendMessage("$p &7Теперь вы &aполучаете&7 сообщения от &aсервера&7!");
        p.playerOptions.chatFromServer = true;
    }
}