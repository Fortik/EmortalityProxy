package ru.crashdami.emortality.threads;

import org.spacehq.packetlib.Session;
import ru.crashdami.emortality.EmortalityMain;
import ru.crashdami.emortality.managers.PlayerManager;
import ru.crashdami.emortality.objects.Player;

public class TabThread extends Thread {

    @Override
    public void run() {
        while (true) {
            if (EmortalityMain.getPlayers().size() >= 1) {
                for (Session session : EmortalityMain.getPlayers()) {
                    final Player p = PlayerManager.getPlayer(session);
                    if (session.isConnected()) {
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
    }
}
