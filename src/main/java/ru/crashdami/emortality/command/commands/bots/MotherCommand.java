package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class MotherCommand extends Command {

    public MotherCommand() {
        super("mother", "Боты повторяют за тобой!", ",mother",
                Group.USER, "follow", "botmother");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isMother()) {
            p.sendMessage("$p &cБоты больше не повторяют ваши движения!");
            p.setMother(false);
            return;
        }
        p.sendMessage("$p &bБоты теперь имитируют ваши движения!");
        p.setMother(true);
    }
}