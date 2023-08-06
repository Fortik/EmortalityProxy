package ru.crashdami.emortality.threads;

import org.spacehq.packetlib.Session;
import ru.crashdami.emortality.EmortalityProxy;
import ru.crashdami.emortality.managers.PlayerManager;
import ru.crashdami.emortality.objects.Player;

public class MSUpdateThread extends Thread {

    @Override
    public void run() {
        while (true) {
            if (EmortalityProxy.getPlayers().size() < 1) return;
            for (Session session : EmortalityProxy.getPlayers()) {
                if (!session.isConnected()) continue;
                final Player p = PlayerManager.getPlayer(session);
                p.updateServerMS();
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}