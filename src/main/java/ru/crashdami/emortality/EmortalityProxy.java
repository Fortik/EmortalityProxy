package ru.crashdami.emortality;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.HandshakeIntent;
import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.command.CommandManager;
import ru.crashdami.emortality.managers.PlayerManager;
import ru.crashdami.emortality.managers.ProxyManager;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.threads.MSUpdateThread;
import ru.crashdami.emortality.threads.TabThread;
import ru.crashdami.emortality.utils.ChatUtilities;
import ru.crashdami.emortality.utils.DateUtilities;
import ru.crashdami.emortality.utils.WebHookUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class EmortalityProxy extends ProxyHelper implements Loader {

    private static List<String> accessPlayers = new ArrayList<>();
    private static List<Session> players = new CopyOnWriteArrayList<>();
    private Server server;
    WebHookUtil webhook = new WebHookUtil("https://discord.com/api/webhooks/1137473993771843717/psdIQUUvu3PEAFHYXU16PHG6FUOVPWqVY42CF5pT9MkdmAwR3l6GRpsnCIRGrKj3f_p2");
    public static List<Session> getPlayers() {
        return players;
    }

    public static void addAccess(String player) {
        accessPlayers.add(player);
    }

    @Override
    public void onLoad() {
        final long msStart = System.currentTimeMillis();
        getLogger().info("Загрузка EmortalityProxy...");
        init();
        loadConfig();
        loadCommands();

        this.server = new Server("0.0.0.0", 25565, MinecraftProtocol.class, new TcpSessionFactory());
        webhook.setContent("emortality proxy started! [bind ip: " + server.getHost() + ":" + server.getPort() + "]");
        try {
            webhook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.server.bind();

        this.server.setGlobalFlag("compression-threshold", 100);
        this.server.setGlobalFlag("verify-users", false);
        this.server.setGlobalFlag("login-handler", new ServerLoginHandler() {
            @Override
            public void loggedIn(final Session session) {
                final GameProfile profile = session.getFlag("profile");
                if (!accessPlayers.contains(profile.getName())) {
                    session.disconnect(ChatUtilities
                            .fixColor("$p &7"));
                    return;
                }
                final Player p = PlayerManager.getPlayer(session);
                if (!p.canJoin()) {
                    session.disconnect(ChatUtilities.fixColor(
                            "$p &cСрок действия вашей подписки истек.\n&7Покупка -> $vk"));
                    webhook.setContent("Игрок " + profile.getName() + " попытался зайти на прокси, но он не имеет подписки.");
                    try {
                        webhook.execute();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                for (Session sPlayer : players) {
                    final GameProfile profilePlayer = sPlayer.getFlag("profile");
                    final String nameS = profilePlayer.getName();
                    if (nameS.equalsIgnoreCase(p.getNick())) {
                        session.disconnect(ChatUtilities.fixColor(
                                "$p &cИгрок с ником " + nameS + " уже на сервере!"));
                    }
                }
                session.send(new ServerJoinGamePacket(1, false,
                        GameMode.SURVIVAL, 0, Difficulty.PEACEFUL, 1000,
                        WorldType.FLAT, false));
                session.send(new ServerSpawnPositionPacket(new Position(
                        0, 1337, 0)));

                session.send(new ServerWorldBorderPacket(15));

                session.send(new ServerPlayerAbilitiesPacket(false, false, false, false, 0.1f, 0.1f));
                session.send(new ServerPlayerPositionRotationPacket(0, 1337, 0, 0.0F, 0.0F));

                p.setSession(session);
                p.setConnected(false);
                p.setLogged(false);
                p.setSessionConnect(null);
                p.setLastPacketMs(0L);
                p.setLastPacket("&c...");
                final Thread t = new TabThread();
                t.start();
                final Thread t2 = new MSUpdateThread();
                t2.start();
                players.add(session);
                p.sendMessage(String.valueOf(getChars()));
                p.sendMessage("$p &7Войдите в аккаунт: &b,login [пароль]");
                session.send(new ServerTitlePacket(
                        ChatUtilities.fixColor("$p &7Войдите в аккаунт: &b,login [пароль]"), true));
                if (!p.can(Group.ADMIN)) { ChatUtilities.broadcast("$p &7Игрок &d[USER] &b" + profile.getName() + " &7подключился к прокси!"); }
                if (p.can(Group.ADMIN)) { ChatUtilities.broadcast("$p &7Администратор &c[ADMIN] &b" + profile.getName() + " &7подключился к прокси!"); }
                webhook.setContent("Игрок " + profile.getName() + " зашел на прокси!");
                try {
                    webhook.execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                session.send(new ServerTitlePacket(20, 28, 20));
                session.send(new ServerSwitchCameraPacket(2));
                session.addListener(new SessionListener() {
                    @Override
                    public void packetReceived(final PacketReceivedEvent event) {
                    }

                    @Override
                    public void packetSent(final PacketSentEvent packetSentEvent) {
                    }

                    @Override
                    public void connected(final ConnectedEvent connectedEvent) {
                    }

                    @Override
                    public void disconnecting(final DisconnectingEvent disconnectingEvent) {
                    }

                    @Override
                    public void disconnected(final DisconnectedEvent ev) {
                        final GameProfile profile = ev.getSession().getFlag("profile");
                        getLogger().info(profile.getName() + " вышел, " +
                                "Причина: " + ev.getReason() + " Помечание: " + ev.getCause());
                        ev.getCause().printStackTrace();
                    }
                });
            }
        });
        this.server.setGlobalFlag("info-builder", new ServerInfoBuilder() {
            @Override
            public ServerStatusInfo buildInfo(final Session session) {

                final GameProfile[] profiles = {new GameProfile(UUID.randomUUID(),
                        ChatUtilities.fixColor("&7Создатель прокси: &bCrashdami\n\n&8>> &7Список активных прокси до 400 мс: " +
                                "&a" + ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies + " \n"))};

                return new ServerStatusInfo(new VersionInfo("§b§lEmortality§f§lProxy                 " +
                        "   §7Онлайн: §a" + players.size() + " ", 47),

                        new PlayerInfo(800, 0, profiles),
                        new TextMessage("§b§lEmortality§f§lProxy §8| §7$vk"), null);
            }
        });
        this.server.addListener(new ServerAdapter() {
            @Override
            public void sessionAdded(final SessionAddedEvent event) {
                event.getSession().addListener(new SessionAdapter() {
                    @Override
                    public void packetReceived(final PacketReceivedEvent event) {

                        //System.out.println(event.getPacket().getClass().getSimpleName());
                        //TODO

                        if (event.getPacket() instanceof HandshakePacket) {
                            final HandshakePacket packet = event.getPacket();
                            if (packet.getIntent() == HandshakeIntent.STATUS) {
                                getLogger().info("Получен пакет от: " + packet.getHostName());
                                ChatUtilities.broadcast("$p &7Получен пакет от: &a" + packet.getHostName());
                            }
                            return;
                        }


                        if (event.getPacket() instanceof ClientPlayerMovementPacket) {
                            final GameProfile profile = event.getSession().getFlag("profile");
                            final Player p = PlayerManager.getPlayer(profile.getName());
                            final ClientPlayerMovementPacket packet = event.getPacket();
                            p.setPosition(new Position((int) packet.getX(), (int) packet.getY(), (int) packet.getZ()));
                            p.setYaw(packet.getYaw());
                            p.setPitch(packet.getPitch());
                            if (p.isMother() && p.isConnected() && p.getSessionConnect() != null) {
                                if (p.getBots().size() > 0) {
                                    for (Bot bot : p.getBots()) {
                                        bot.getSession().send(event.getPacket());
                                    }
                                }
                            }
                            return;
                        }
                        if (event.getPacket() instanceof ClientWindowActionPacket ||
                                        event.getPacket() instanceof ClientSwingArmPacket ||
                                        event.getPacket() instanceof ClientPlayerPlaceBlockPacket ||
                                        event.getPacket() instanceof ClientRequestPacket ||
                                        event.getPacket() instanceof ClientPlayerStatePacket ||
                                        event.getPacket() instanceof ClientChangeHeldItemPacket ||
                                        event.getPacket() instanceof ClientCloseWindowPacket ||
                                        event.getPacket() instanceof ClientPlayerRotationPacket ||
                                        event.getPacket() instanceof ClientConfirmTransactionPacket ||
                                        event.getPacket() instanceof ClientPlayerActionPacket ||
                                        event.getPacket() instanceof ClientCreativeInventoryActionPacket ||
                                        event.getPacket() instanceof ClientSettingsPacket ||
                                        event.getPacket() instanceof ClientSteerVehiclePacket ||
                                        event.getPacket() instanceof ClientTabCompletePacket) {
                            final GameProfile profile = event.getSession().getFlag("profile");
                            final Player p = PlayerManager.getPlayer(profile.getName());
                            if (p.isMother() && p.isConnected() && p.getSessionConnect() != null) {
                                if ((event.getPacket() instanceof ClientChatPacket &&
                                        ((ClientChatPacket) event.getPacket()).getMessage().startsWith(","))) return;
                                if (p.getBots().size() > 0) {
                                    for (Bot bot : p.getBots())
                                        bot.getSession().send(event.getPacket());
                                }
                            }
                        }
                        if (event.getPacket() instanceof ClientChatPacket) {
                            final GameProfile profile = event.getSession().getFlag("profile");
                            final Player p = PlayerManager.getPlayer(profile.getName());
                            final ClientChatPacket packet = event.getPacket();
                            final Session s = event.getSession();
                            final String[] args = packet.getMessage().split(" ");
                            if ((!p.isLogged() && args[0].equalsIgnoreCase(",login")) ||
                                    args[0].equalsIgnoreCase(",l")) {
                                if (args.length < 2) {
                                    p.sendMessage("$p &cПравильное использование: &7!,login [паролт]");
                                    s.send(new ServerChatPacket(
                                            ChatUtilities.fixColor("$p &cПравильное использование: " +
                                                    "&7,login [пароль]"), MessageType.NOTIFICATION));
                                    return;
                                }
                                if (p.isLogged()) {
                                    p.sendMessage("$p &cВы уже вошли в аккаунт!");
                                    return;
                                }
                                final String pass = args[1];
                                if (pass.equalsIgnoreCase(p.getPassword())) {
                                    p.setLogged(true);
                                    p.sendMessage("$p &aАвторизация успешна!");
                                    webhook.setContent("Игрок " + profile.getName() + " успешно вошел в аккаунт!");
                                    try {
                                        webhook.execute();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                    s.send(new ServerChatPacket(ChatUtilities.fixColor(
                                            "&7Привет, &b" + profile.getName() + "!"),
                                            MessageType.NOTIFICATION));
                                    p.sendMessage("$p &7Привет, &b" + profile.getName() + "! " +
                                            "&7Доступные команды: ,help");
                                    p.sendMessage("$p &aПодписка активна до: &6" +
                                            DateUtilities.getDate(p.getExpirationDate()));
                                    s.send(new ServerTitlePacket(ChatUtilities.fixColor("&aПодписка активна до: " +
                                            "&2" + DateUtilities.getDate(p.getExpirationDate())), true));
                                    s.send(new ServerTitlePacket(20, 27, 20));
                                    s.send(new ServerTitlePacket(ChatUtilities.fixColor("&8## &bEmortality&fProxy &8##"), false));
                                } else {
                                    s.disconnect(ChatUtilities.fixColor("$p &cНеверный пароль."));
                                }
                            } else {
                                if (!p.isLogged() && !args[0].equalsIgnoreCase(",login")
                                        && !args[0].equalsIgnoreCase(",l")) {
                                    p.sendMessage("$p &cВойдите в аккаунт! (,login [пароль])");
                                    s.send(new ServerChatPacket(ChatUtilities.fixColor(
                                            "$p &cВойдите в аккаунт! (,login [пароль])"), MessageType.NOTIFICATION));
                                    return;
                                }
                                if (packet.getMessage().startsWith(",")) {
                                    final String arg = args[0].replace(",", "");
                                    for (Command cmd : CommandManager.getCommands()) {
                                        if (cmd.getCommand().equalsIgnoreCase(arg) ||
                                                Arrays.asList(cmd.getAliases()).contains(arg)) {
                                            cmd.onCommand(p, cmd, args);
                                        }
                                    }
                                    return;
                                } else if (packet.getMessage().startsWith("@")) {
                                    for (Session sessionAll : server.getSessions()) {
                                        sessionAll.send(new ServerChatPacket(
                                                ChatUtilities.fixColor("$p "
                                                        + p.getGroup().getPrefix() + " " + profile.getName() + " §8» &6"
                                                        + p.getGroup().getSuffix() + packet.getMessage().replace(
                                                        "@", ""))));
                                    }
                                }
                            }
                        }
                    }
                });
            }

            @Override
            public void sessionRemoved(final SessionRemovedEvent event) {
                final GameProfile profile = event.getSession().getFlag("profile");
                final Player p = PlayerManager.getPlayer(profile.getName());
                players.remove(event.getSession());
                ChatUtilities.broadcast(
                        "$p &a" + p.getNick() + "&7 opuscil proxy!");
            }
        });
        final long ms = (System.currentTimeMillis() - msStart);
        getLogger().info("Загрузка прокси завершена за: " + ms + "ms!");
        System.out.println("Сервер доступен по адресу: " + server.getHost() + ":" + server.getPort());
        while (true) {
        }
    }
}
