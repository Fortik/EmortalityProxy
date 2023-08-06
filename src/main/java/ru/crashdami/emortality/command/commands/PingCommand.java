package ru.crashdami.emortality.command.commands;

import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.TcpSessionFactory;
import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

import java.net.Proxy;

public class PingCommand extends Command {

    public PingCommand() {
        super("ping", "Пропинговать сервер!", ",ping [сервер:порт]",
                Group.USER, "zrobping");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2 || !args[1].contains(":")) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        }
        //final String host = args[1].split(":")[0];
        /*String host;
        Integer port;
        final String argIp = args[1];
        try {
            host = SRVResolver.srv(args[1].split(":")[0]);
            port = Integer.parseInt(host.split(":")[1]);
        }
        catch (ArrayIndexOutOfBoundsException e){
            host = argIp.split(":")[0];
            port = Integer.parseInt(argIp.split(":")[1]);
        }*/
        p.sendMessage("$p &bПингование...");
        final long ms = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
                final Client client = new Client(args[1].split(":")[0], Integer.parseInt(args[1].split(":")[1]), protocol, new TcpSessionFactory(Proxy.NO_PROXY));
                client.getSession().setConnectTimeout(p.playerOptions.timeOutPing);
                client.getSession().setFlag("server-info-handler", new ServerInfoHandler() {
                    @Override
                    public void handle(final Session session, final ServerStatusInfo info) {
                        p.sendMessage("$p &7Успешно!. &b[ Ядро сервера: &7" + info.getVersionInfo().getVersionName() + "&b, игроков: &7"
                                + info.getPlayerInfo().getOnlinePlayers() + "&8/&7" + info.getPlayerInfo().getMaxPlayers() + "&b, описание: &7" +
                                info.getDescription().getFullText() + " &b]");
                        p.sendMessage("$p &bСервер ответил на пинг за: &7" + (System.currentTimeMillis() - ms) + "ms");
                        client.getSession().disconnect("успешно.");
                    }
                });
                client.getSession().connect();
            }
        }).start();
    }
}