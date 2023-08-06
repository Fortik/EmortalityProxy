package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class MessagesBotCommand extends Command {

    public MessagesBotCommand() {
        super("messagesbot", "Сообщения при заходе", ",messagesbot [quit/join]",
                Group.USER, "botmessages");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("quit") || type.equalsIgnoreCase("leave")) {
            if (p.botOptions.quit) {
                p.sendMessage("$p &7Сообщения о выходе с сервера ботом: &cвыключены&7!");
                p.botOptions.quit = false;
                return;
            }
            p.sendMessage("$p &7Сообщения о выходе с сервера ботом: &aвключены&7!");
            p.botOptions.quit = true;
        } else {
            if (p.botOptions.join) {
                p.sendMessage("$p &7Сообщения о входе на сервер ботом: &cвыключены&7!");
                p.botOptions.quit = false;
                return;
            }
            p.sendMessage("$p &7Сообщения о входе на сервер ботом: &aвключены&7!");
            p.botOptions.join = true;
        }
    }
}