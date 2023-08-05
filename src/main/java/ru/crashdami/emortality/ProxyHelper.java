package ru.crashdami.emortality;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import ru.crashdami.emortality.command.commands.*;
import ru.crashdami.emortality.command.commands.bots.*;
import ru.crashdami.emortality.command.commands.settings.*;
import ru.crashdami.emortality.enums.Group;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.utils.DateUtilities;
import ru.crashdami.emortality.managers.PlayerManager;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class ProxyHelper {

    private static Logger logger = Logger.getLogger("EmortalityProxy");
    private char[] chars;
    private XMLConfiguration config;

    protected char[] getChars() {
        return chars;
    }

    public static Logger getLogger() {
        return logger;
    }

    protected void init() {
        Arrays.fill(chars = new char[7680], ' ');
    }

    protected void loadConfig() {
        if (!new File("EmortalityProxy").exists()) {
            new File("EmortalityProxy").mkdirs();
        }
        if (!new File("EmortalityProxy", "settings.xml").exists()) {
            final XMLConfiguration configCreate = new XMLConfiguration();
            configCreate.setBasePath("EmortalityProxy");
            configCreate.setFileName("settings.xml");
            configCreate.addProperty("users", Arrays.asList("Crashdami;ADMIN;haslodoproxy;10-02-2019:14:00:00"));
            this.config = configCreate;
        } else {
            final XMLConfiguration configCreate = new XMLConfiguration();
            try {
                configCreate.load(new File("EmortalityProxy", "settings.xml"));
            } catch (ConfigurationException ex) {
                ex.printStackTrace();
            }
            this.config = configCreate;
        }
        this.config.setBasePath("EmortalityProxy");
        this.config.setFileName("settings.xml");
        try {
            config.save();
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        }
        try {
            final XMLConfiguration config2 = new XMLConfiguration("EmortalityProxy/settings.xml");
            for (int i = 0; i < config2.getStringArray("users").length; ++i) {
                final String[] array = config2.getStringArray("users");
                final String[] split = array[i].split(";");
                final Player p = PlayerManager.getPlayer(split[0]);
                p.setGroup(Group.valueOf(split[1]));
                p.setPassword(split[2]);
                p.setExpirationDate(DateUtilities.getDateFromString(split[3]));
                EmortalityMain.addAccess(p.getNick());
            }
            this.config = config2;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    protected void loadCommands() {
        new ConnectCommand();
        new CrashCommand();
        new ConnectBotCommand();
        new QuitCommand();
        new DetachCommand();
        new BotListCommand();
        new BotChatCommand();
        new MotherCommand();
        new TimeOutCommand();
        new HelpCommand();
        new PingCommand();
        new BotQuitCommand();
        new StopCommand();
        new ChatFromBotCommand();
        new ChatFromServerCommand();
        new AutoReconnectCommand();
        new AutoReconnectTimeCommand();
        new AutoCaptchaCommand();
        new MacroCommand();
        new AutoLoginCommand();
        new MessagesBotCommand();
        new ProxyCommand();
        //new TestObejscieCommand();
    }

}
