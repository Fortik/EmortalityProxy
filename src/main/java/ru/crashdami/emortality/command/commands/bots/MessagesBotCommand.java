package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class MessagesBotCommand extends Command {

    public MessagesBotCommand() {
        super("messagesbot", "Включение/выключение сообщений о подключении бота", ",messagesbot [quit/join]",
                Group.PLAYER, "botmessages");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Poprawne uzycie: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("quit") || type.equalsIgnoreCase("leave")) {
            if (p.botOptions.quit) {
                p.sendMessage("$p &7Wiadomosci o wyjsciu z serwera przez boty: &cwylaczone&7!");
                p.botOptions.quit = false;
                return;
            }
            p.sendMessage("$p &7Wiadomosci o wyjsciu z serwera przez boty: &awlaczone&7!");
            p.botOptions.quit = true;
        } else {
            if (p.botOptions.join) {
                p.sendMessage("$p &7Wiadomosci o dolaczeniu do serwera przez boty: &cwylaczone&7!");
                p.botOptions.quit = false;
                return;
            }
            p.sendMessage("$p &7Wiadomosci o dolaczeniu do serwera przez boty: &awlaczone&7!");
            p.botOptions.join = true;
        }
    }
}