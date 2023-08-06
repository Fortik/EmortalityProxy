package ru.crashdami.emortality.objects;

import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.packetlib.Session;
import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.managers.ProxyManager;
import ru.crashdami.emortality.objects.player.BotOptions;
import ru.crashdami.emortality.objects.player.Connector;
import ru.crashdami.emortality.objects.player.PlayerOptions;
import ru.crashdami.emortality.utils.ChatUtilities;
import ru.crashdami.emortality.utils.DateUtilities;

import java.util.LinkedList;
import java.util.List;

public class Player extends Connector {

    public boolean stopMacroBot;
    public boolean stopMacroPlayer;
    public boolean macroRecording;
    public Macro macro;
    public PlayerOptions playerOptions;
    public BotOptions botOptions;
    public Thread crashPlayerThread, crashBotsThread, chatBotsSpamThread;
    private String nick;
    private Group group;
    private Session session;
    private long expirationDate;
    private String password;
    private boolean logged;
    private boolean mother;
    private List<Bot> bots = new LinkedList<>();

    public Player(String name, Group group) {
        this.nick = name;
        this.group = group;
        this.playerOptions = new PlayerOptions();
        this.botOptions = new BotOptions();
    }

    public boolean can(Group group) {
        return this.group.getPermissionLevel() >= group.getPermissionLevel();
    }

    public void sendMessage(String message) {
        this.session.send(new ServerChatPacket(ChatUtilities.fixColor(message)));
    }

    public void updateTab() {
        if (sessionConnect != null && !getLastPacket().toLowerCase().contains("...")) {
            final Long ms = (System.currentTimeMillis() - getLastPacketMs() == 0L) ? 1L :
                    (System.currentTimeMillis() - getLastPacketMs());
            session.send(new ServerChatPacket(
                    ChatUtilities.fixColor("&8>> &7Последний пакет: &a" + ms + "ms"),
                    MessageType.NOTIFICATION));
            session.send(new ServerPlayerListDataPacket(
                    new TextMessage(ChatUtilities.fixColor("&b&lEmortality&f&lProxy &7by &bCrashdami\n")),
                    new TextMessage(ChatUtilities.fixColor("\n&7Последний полученый пакет &8(&7" + (System.currentTimeMillis() - getLastPacketMs()
                    ) + "ms&8):\n&8( &b" + getLastPacket() + "&8 )\n&7Сессия: &b" + nick + " / " + sessionConnect.getHost() + "" +
                            "\n&7Подписка активна до: &b" + DateUtilities.getDate(getExpirationDate()) + "" +
                            "\n&7Сервер: &b" +
                            sessionConnect.getHost() + ":" + sessionConnect.getPort() + "\n " +
                            "&7Количество ботов: &b" + bots.size() + "\n&7Количество активных прокси: &b" +
                            ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies +
                            "\n\n&b$vk &8| &b$ds"))));
        } else if (sessionConnect == null || getLastPacket().toLowerCase().contains("rozlaczono")) {
            session.send(new ServerPlayerListDataPacket(
                    new TextMessage(ChatUtilities.fixColor("&b&lEmortality&f&lProxy &7by &bCrashdami\n")),
                    new TextMessage(ChatUtilities.fixColor("\n&7Последний полученый пакет &8(&70ms&8):\n&8( &c... &8)" +
                            "\n&7Сессия: &b" + nick + " / $ip" + "" +
                            "\n&7Подписка активна до: &b" + DateUtilities.getDate(getExpirationDate()) + "\n&7Сервер: $ip" + "\n " +
                            "&7Количество ботов: &a" + bots.size() + "\n&7Количество активных прокси: &a" +
                            ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies +
                            "\n\n&b$vk &8| &b$ds"))));
        }
    }

    public void updateServerMS() {
        if (sessionConnect != null && System.currentTimeMillis() - getLastPacketMs() > 1050L
                && System.currentTimeMillis() - getLastPacketMs() < 1000000L) {
            session.send(new ServerTitlePacket("", false));
            session.send(new ServerTitlePacket(ChatUtilities.fixColor("&8>> &cСервер не отвечает! (LAG?)"), false));
            session.send(new ServerTitlePacket(ChatUtilities.fixColor("&8>> &c" + (System.currentTimeMillis()
                    - getLastPacketMs()) + "ms"), true));
            session.send(new ServerTitlePacket(10, 17, 10));
        }
    }

    public boolean canJoin() {
        return this.expirationDate > System.currentTimeMillis();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNick() {
        return nick;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public List<Bot> getBots() {
        return bots;
    }

    public void addBot(Bot bot) {
        this.bots.add(bot);
    }

    public void removeBot(Bot bot) {
        this.bots.remove(bot);
    }

    public boolean isMother() {
        return mother;
    }

    public void setMother(boolean mother) {
        this.mother = mother;
    }
}
