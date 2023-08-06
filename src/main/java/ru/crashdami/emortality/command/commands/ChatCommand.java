package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.utils.ChatUtilities;

public class ChatCommand extends Command {

    public ChatCommand() {
        super("chat", "Написать сообщение в чат!", ",chat [сообщение]",
                Group.USER, "msg", "chatik");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (!p.can(Group.ADMIN)) { ChatUtilities.broadcast("$p &d[USER] &b" + p.getNick() + "&f: " + type); }
        if (p.can(Group.ADMIN)) { ChatUtilities.broadcast("$p &c[ADMIN] &b" + p.getNick() + "&f: " + type); }
    }
}