package ru.crashdami.emortality;

import ru.crashdami.emortality.managers.ProxyManager;

public class Proxy {

    public Proxy() {
        ProxyManager.loadProxies(false, 20000);
    }

    public static void main(String[] args) {
        new Proxy();
        final EmortalityProxy proxy = new EmortalityProxy();
        proxy.onLoad();
    }

    /*private void checkWWW() throws RuntimeException {
        try {
            URL oracle = new URL("https://raw.githubusercontent.com/yooniks/proxy_license/master/license.txt");
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase("true")) {
                    System.out.println(
                            "\n\n\n\n\n\n\n\n\n\n\n\n" +
                                    "##################################" +
                                    "\n\n" +
                                    "Licencja poprawna!" +
                                    "\n\n" +
                                    "#########################");
                } else {
                    throw new RuntimeException("вырежу это нахуй");
                }
            }
            in.close();
        } catch (Throwable ex) {
            throw new RuntimeException("вырежу это нахуй");
        }
    }*/
}
