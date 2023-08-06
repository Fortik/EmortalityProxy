package ru.crashdami.emortality.threads;

import org.spacehq.packetlib.Session;
import ru.crashdami.emortality.EmortalityProxy;
import ru.crashdami.emortality.managers.PlayerManager;
import ru.crashdami.emortality.objects.Player;

public class TabThread extends Thread {

    @Override
    public void run() {
        while (true) {
            if (EmortalityProxy.getPlayers().size() < 1) return;
            for (Session session : EmortalityProxy.getPlayers()) {
                if (!session.isConnected()) continue;
                final Player p = PlayerManager.getPlayer(session);
                p.updateTab();
                try {
                    Thread.sleep(20L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}