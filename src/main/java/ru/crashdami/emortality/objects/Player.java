package ru.crashdami.emortality.objects;

import org.spacehq.mc.protocol.data.game.values.MessageType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.packet.Packet;
import ru.crashdami.emortality.objects.player.BotOptions;
import ru.crashdami.emortality.objects.player.Connector;
import ru.crashdami.emortality.objects.player.PlayerOptions;
import ru.crashdami.emortality.enums.Group;
import ru.crashdami.emortality.managers.ProxyManager;
import ru.crashdami.emortality.utils.ChatUtilities;

import java.util.LinkedList;
import java.util.List;

public class Player extends Connector {

    /*private boolean stopCrash;
    private boolean stopChatBot;
    private boolean stopCrashBot;*/
    public boolean stopMacroBot; //to tez kiedys poprawie, jak bedzie mi sie chcialo xd
    public boolean stopMacroPlayer; //to tez kiedys poprawie, jak bedzie mi sie chcialo xd
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
        this.session.send((Packet) new ServerChatPacket(ChatUtilities.fixColor(message)));
    }

    public void updateTab() {
        if (sessionConnect != null && !getLastPacket().toLowerCase().contains("rozlaczono")) {
            final Long ms = (System.currentTimeMillis() - getLastPacketMs() == 0L) ? 1L :
                    (System.currentTimeMillis() - getLastPacketMs());
            session.send((Packet) new ServerChatPacket(
                    ChatUtilities.fixColor("&8>> &7Последний пакет с сервера был получен: &a" + ms + "ms назад"),
                    MessageType.NOTIFICATION));
            session.send((Packet) new ServerPlayerListDataPacket(
                    new TextMessage(ChatUtilities.fixColor("&c⦓&m-----&r &bEmortality&fProxy &7created by &aCrashdami&7, &avelocitylove&c&m----⦔&r\n")),
                    new TextMessage(ChatUtilities.fixColor("\n&7Последний пакет с сервера &8(&7" + (System.currentTimeMillis() - getLastPacketMs()
                    ) + "ms назад&8):\n&8( &a" + getLastPacket() + "&8 )\n&7Никнейм: &a" + nick + "\n&7Айпи сервера: &a" +
                            sessionConnect.getHost() + ":" + sessionConnect.getPort() + "\n " +
                            "&7Количество ботов: &a" + bots.size() + "\n&7Список активных прокси: &a" +
                            ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies +
                            "\n\n&c⦓&m-----&r &7Покупка доступа:" +
                            " &bhttps://vk.com/emortality &c&m----⦔&r"))));
        } else if (sessionConnect == null || getLastPacket().toLowerCase().contains("rozlaczono")) {
            session.send((Packet) new ServerPlayerListDataPacket(
                    new TextMessage(ChatUtilities.fixColor("&c⦓&m-----&r &bEmortality&fProxy &7created by &aCrashdami&7, &avelocitylove &c&m----⦔&r\n")),
                    new TextMessage(ChatUtilities.fixColor("\n&7Последний пакет с сервера &8(&70ms назад&8):\n&8( &cОтключено &8)\n&7Никнейм: &a" + nick + "\n&7Айпи сервера &a" +
                            "отсутсвует" + "\n " +
                            "&7Количество ботов: &a" + bots.size() + "\n&7Список активных прокси: &a" +
                            ProxyManager.proxies.size() + "&8/&c" + ProxyManager.allproxies +
                            "\n\n&c⦓&m-----&r &7Покупка доступа:" +
                            " &bhttps://vk.com/emortality &c&m----⦔&r"))));
        }
    }

    public void updateLag() {
        if (sessionConnect != null && System.currentTimeMillis() - getLastPacketMs() > 1050L
                && System.currentTimeMillis() - getLastPacketMs() < 1000000L) {
            session.send((Packet) new ServerTitlePacket("", false));
            session.send((Packet) new ServerTitlePacket(ChatUtilities.fixColor("&8>> &cСервер не отвечает (LAG?)"), false));
            session.send((Packet) new ServerTitlePacket(ChatUtilities.fixColor("&8>> &fОт: &7" + (System.currentTimeMillis()
                    - getLastPacketMs()) + "ms"), true));
            session.send((Packet) new ServerTitlePacket(10, 17, 10));
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

    /*public boolean isStopCrash() {
        return stopCrash;
    }

    public boolean isStopChatBot() {
        return stopChatBot;
    }

    public boolean isStopCrashBot() {
        return stopCrashBot;
    }

    public void setStopChatBot(boolean stopChatBot) {
        this.stopChatBot = stopChatBot;
    }

    public void setStopCrash(boolean stopCrash) {
        this.stopCrash = stopCrash;
    }

    public void setStopCrashBot(boolean stopCrashBot) {
        this.stopCrashBot = stopCrashBot;
    }*/
}
