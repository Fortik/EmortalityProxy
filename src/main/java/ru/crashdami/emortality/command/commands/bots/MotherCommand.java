package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.enums.Group;

public class MotherCommand extends Command {

    public MotherCommand() {
        super("mother", "Boty nasladuja twoje ruchy!", ",mother",
                Group.PLAYER, "follow", "botmother");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isMother()) {
            p.sendMessage("$p &cOd teraz boty nie nasladuja twoich ruchow!");
            p.setMother(false);
            return;
        }
        p.sendMessage("$p &aOd teraz boty nasladuja twoje ruchy!");
        p.setMother(true);
    }
}