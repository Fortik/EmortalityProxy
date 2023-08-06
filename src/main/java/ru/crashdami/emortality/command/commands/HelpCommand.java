package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.command.CommandManager;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Отображает все команды", ",help",
                Group.USER, "pomoc", "pomocy");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        p.sendMessage("&7Список команд: &8(&b" + CommandManager.getCommands().size() + "&8)");
        for (Command cmd : CommandManager.getCommands())
            p.sendMessage("&b» ," + cmd.getCommand() + "&7 - " + cmd.getDescription());
    }
}