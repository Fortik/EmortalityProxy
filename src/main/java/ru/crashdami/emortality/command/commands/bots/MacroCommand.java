package ru.crashdami.emortality.command.commands.bots;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.managers.MacroManager;
import ru.crashdami.emortality.objects.Macro;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.types.MacroType;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MacroCommand extends Command {

    public MacroCommand() {
        super("macro", "Запишите свои движения, а затем повторите их!", ",macro",
                Group.USER, "makro");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &7Правильное использование: &b,macro start [bots/runnables]");
            p.sendMessage("&b,macro play [айди макро] [тип макро bot/human] [будет ли макрос проигроваться бесконечно?, true/false]");
            p.sendMessage("&b,macro stop&7 - остановить все макро");
            p.sendMessage("&b,stop &7- команды для остановки выполнения макро");
            return;
        }
        if (args[1].equalsIgnoreCase("start")) {
            if (args.length == 3) {
                if (p.macroRecording) {
                    p.sendMessage("$p &cВы уже записываете макрос! Вы можете отключить запись макроса, используя: &6,macro stop");
                    return;
                }
                if (p.getSessionConnect() == null) {
                    p.sendMessage("$p &cВы не подключены ни к одному серверу!");
                    return;
                }
                if (args[2].equalsIgnoreCase("bot")) {
                    final Random rand = new Random();
                    int id = rand.nextInt(400);
                    for (final Macro m : MacroManager.macros) {
                        if (m.getId() == id) {
                            id = rand.nextInt(410);
                        }
                    }
                    final Macro macro = new Macro(id, MacroType.BOT, p.getNick());
                    p.sendMessage("$p &bНачинаю запись макроса.. Айди макро: &7" + macro.getId() + "&b, Тип макро: &7BOT");
                    p.macro = macro;
                    macro.startRecording(p);
                } else if (args[2].equalsIgnoreCase("runnables")) {
                    final Random rand = new Random();
                    int id = rand.nextInt(400);
                    for (final Macro m : MacroManager.macros) {
                        if (m.getId() == id) {
                            id = rand.nextInt(410);
                        }
                    }
                    final Macro macro = new Macro(id, MacroType.PLAYER, p.getNick());
                    p.sendMessage("$p &bНачинаю запись макроса.. Айди макро: &7" + macro.getId() + "&b, Тип макро: &7PLAYER");
                    p.macro = macro;
                    macro.startRecording(p);
                }
            } else {
                p.sendMessage("$p &7Правильное использование: &b,macro start [bots/runnables]");
                p.sendMessage("&b,macro play [айди макро] [тип макро bot/human] [будет ли макрос проигроваться бесконечно?, true/false]");
                p.sendMessage("&b,macro stop&7 - остановить все макро");
                p.sendMessage("&b,stop &7- команды для остановки выполнения макро");
            }
        } else if (args[1].equalsIgnoreCase("play")) {
            if (args.length > 4) {
                Macro macro2 = null;
                for (final Macro m : MacroManager.macros) {
                    if (m.getId() == Integer.valueOf(args[2])) {
                        macro2 = m;
                    }
                }
                if (macro2 == null) {
                    p.sendMessage("$p &cМакро с айди &6" + args[2] + " &cне найден!");
                    return;
                }
                final boolean infinite = Boolean.valueOf(args[4]);
                final String type = args[3];
                if (type.equalsIgnoreCase("runnables") || type.equalsIgnoreCase("human")) {
                    if (p.getSessionConnect() == null) {
                        p.sendMessage("$p &cВы не подключены ни к одному серверу!");
                        return;
                    }
                    p.sendMessage("$p &bМакрос запущен!, ID: &7" + macro2.getId() + "&b, Тип макро: " +
                            "&7PLAYER&b, Бесконечно: &7" + infinite + "&b, остановить макро: &7,stop macro");
                    macro2.macroStartDoing(p, infinite);
                } else if (type.equalsIgnoreCase("bot") || type.equalsIgnoreCase("bots")) {
                    if (p.getBots().size() < 1) {
                        p.sendMessage("$p &cВы еще не запустили не одного бота!");
                        return;
                    }
                    p.sendMessage("$p &bМакрос запущен!, ID: &7" + macro2.getId() + "&b, Тип макро: " +
                            "&7BOT&b, Бесконечно: &7" + infinite + "&b, остановить макро: &7,stop macrobot");
                    macro2.macroStartDoing(p, infinite);
                }
            }
        } else if (args[1].equalsIgnoreCase("stop")) {
            if (!p.macroRecording) {
                p.sendMessage("$p &cВы не записываете макро! Вы можете начать запись макро, используя: &6,macro start");
                return;
            }
            if (p.macro != null) {
                p.sendMessage("$p &bМакрос записан и готов к использованию. &b("
                        + p.macro.getPackets().size() + "&b пакетов, &bдлительность: "
                        + p.macro.getTime() + "ms (" + TimeUnit.MILLISECONDS.toSeconds(p.macro.getTime()) + "сек)&b, айди: &7" + p.macro.getId() + "&b)");
                p.macro.stopRecording(p);
                //p.macroRecording=false;
                //MacroManager.macros.add(p.macro);
                //p.macro=null;
            }
        } else if (args[1].equalsIgnoreCase("list")) {
            if (MacroManager.macros.size() == 0) {
                p.sendMessage("$p &cУ вас нету записаных макро!");
                return;
            }
            p.sendMessage("$p &cСписок макро:");
            for (Macro macro : MacroManager.macros)
                p.sendMessage("&6" + macro.getId() + "&7, Тип: &6" + macro.getMacroType()
                        + "&7, Количество пакетов: &6" + macro.getPackets().size() + "&7, длительность: &6"
                        + macro.getTime() + "ms (" + TimeUnit.MILLISECONDS.toSeconds(p.macro.getTime()) + "сек)&7, автор: &6" + macro.getOwner());

        }
    }
}