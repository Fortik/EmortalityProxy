package ru.crashdami.emortality.command;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;

public abstract class Command implements CommandExecutor {

    private Group group;
    private String[] aliases;
    private String description;
    private String command;
    private String usage;

    public Command(String command, String description, String usage, Group group, String... aliases) {
        this.command = command;
        this.description = description;
        this.group = group;
        this.aliases = aliases;
        this.usage = usage;
        CommandManager.addCommand(this);
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (!p.can(group)) {
            p.sendMessage("Вы не авторизованы для этой команды!");
            return;
        }
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }
}
