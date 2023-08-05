package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.command.CommandManager;
import ru.crashdami.emortality.enums.Group;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Wyswietla wszystkie komendy", ",help",
                Group.PLAYER, "pomoc", "pomocy");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        p.sendMessage("&7Lista komend: &8(&a" + CommandManager.getCommands().size() + "&8)");
        for (Command cmd : CommandManager.getCommands())
            p.sendMessage("&a," + cmd.getCommand() + "&7 - " + cmd.getDescription());
    }
}