package ru.crashdami.emortality.command.commands;

import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.managers.ProxyManager;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;

public class ProxyCommand extends Command {

    public ProxyCommand() {
        super("proxy", "Управление ip-прокси!", ",proxy [recheck/list]",
                Group.USER, "proksi");
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {
        if (args.length < 2) {
            p.sendMessage("$p &a,proxy [recheck/list] &7- правильное использование");
            return;
        }
        /*if (!p.can(Group.ADMIN)) {
            if (args[1].equalsIgnoreCase("list") && args.length < 3) {
                if (ProxyManager.proxies.size() <= 0) {
                    return;
                }
                p.sendMessage("$p &cЛист прокси: &a( " + ProxyManager.proxies.size() + " )");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (final objects.ru.crashdami.emortality.Proxy proxy : ProxyManager.proxies) {
                            proxy.getInfo(p);
                        }
                    }
                }).start();
            }
        }*/
        else {
            if (args[1].equalsIgnoreCase("recheck")) {
                if (!p.can(Group.ADMIN)) {
                    p.sendMessage("$p &cУ вас нет доступа к этой команде!");
                    return;
                }
                p.sendMessage("$p &aПерезагрузка прокси...");
                ProxyManager.proxies.clear();
                ProxyManager.loadProxies(true, 400);
            }
        }
    }
}