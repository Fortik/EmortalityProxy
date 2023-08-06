package ru.crashdami.emortality.command.commands;

import org.spacehq.mc.protocol.data.game.values.ClientRequest;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class RespawnCommand extends Command {

    public RespawnCommand() {
        super("respawn", "Отправляет пакет респавна клиенту", ",respawn",
                Group.USER, "respawnchuj");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (p.isConnected()) {
            p.getSessionConnect().send(new ClientRequestPacket(ClientRequest.RESPAWN));
            p.sendMessage("$p &aЗапрос на возрождение отправлен!");
        } else {
            p.sendMessage("$p &cВы не подключены ни к одному серверу!");
        }
    }
}