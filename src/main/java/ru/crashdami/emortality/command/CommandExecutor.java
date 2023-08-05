package ru.crashdami.emortality.command;

import ru.crashdami.emortality.objects.Player;

public interface CommandExecutor {

    public abstract void onCommand(Player p, Command command, String[] args);
}
