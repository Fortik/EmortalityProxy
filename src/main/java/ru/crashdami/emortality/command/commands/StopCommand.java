package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.objects.Player;

public class StopCommand extends Command {

    public StopCommand() {
        super("stop", "Остановить атаку!", ",stop [chatbot, crashbot, crash, macrobot, macro]",
                Group.USER, "stopcrash", "stopbot");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &a" + getUsage());
            return;
        }
        final String type = args[1];
        if (type.equalsIgnoreCase("chatbot") || type.equalsIgnoreCase("botchat")) {
            if (p.chatBotsSpamThread != null) {
                p.sendMessage("$p &cПопытка остановить чатботов..");
                p.chatBotsSpamThread.stop();
                return;
            } else {
                p.sendMessage("$p &cТакой ветки не существует!");
            }
        } else if (type.equalsIgnoreCase("crashbot") || type.equalsIgnoreCase("botcrash")) {
            if (p.crashBotsThread != null) {
                p.sendMessage("$p &cПопытка остановить крашботов..");
                p.crashBotsThread.stop();
                return;
            } else {
                p.sendMessage("$p &cТакой ветки не существует!");
            }
        } else if (type.equalsIgnoreCase("crash") || type.equalsIgnoreCase("crashplayer")) {
            if (p.crashPlayerThread != null) {
                p.sendMessage("$p &cПопытка остановить краш..");
                p.crashPlayerThread.stop();
                return;
            } else {
                p.sendMessage("$p &cТакой ветки не существует!");
            }
        } else if (type.equalsIgnoreCase("macrobot") || type.equalsIgnoreCase("botmacro")) {
            if (!p.stopMacroBot) { //to kiedys tez zedytuje xd
                p.sendMessage("$p &cПопытка остановить макробот..");
                p.stopMacroBot = true;
                return;
            } else {
                p.sendMessage("$p &cЭта опция уже выключена!");
            }
        } else if (type.equalsIgnoreCase("macro") || type.equalsIgnoreCase("macroplayer")) {
            if (!p.stopMacroPlayer) {
                p.sendMessage("$p &cПопытка остановить макро.");
                p.stopMacroPlayer = true;
                return;
            } else {
                p.sendMessage("$p &cЭта опция уже выключена!");
            }
        } else {
            p.sendMessage("$p &cНеверный тип! &7(" + type + ")");
        }
    }
}