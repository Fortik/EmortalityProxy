package ru.crashdami.emortality;

import ru.crashdami.emortality.exceptions.InvalidLicenseReturnException;
import ru.crashdami.emortality.managers.ProxyManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Proxy extends JFrame {

    private JPanel panel;

    public Proxy() {
        ProxyManager.loadProxies(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Proxy().setVisible(true);
        });
        final EmortalityMain proxy = new EmortalityMain();
        proxy.onLoad();
    }
}
