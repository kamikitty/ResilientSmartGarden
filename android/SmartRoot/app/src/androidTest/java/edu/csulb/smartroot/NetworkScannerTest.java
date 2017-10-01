package edu.csulb.smartroot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.wifi.WifiInfo;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;

import java.net.*;

import edu.csulb.smartroot.welcome.Welcome;

/**
 * An instrumented test that will test scanning a network.
 */
@RunWith(AndroidJUnit4.class)
public class NetworkScannerTest {

    @Rule
    public ActivityTestRule<Welcome> activityTestRule =
            new ActivityTestRule<>(Welcome.class);

    @Test
    public void WiFiTest() {
        WifiManager wifiManager;

        Log.d("WIFI TEST", "Getting WIFI");
        wifiManager = (WifiManager) activityTestRule.getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        // Get IP address of Android device
        int ip = wifiInfo.getIpAddress();

        // Log state of WiFi
        Log.d("WIFI TEST", "State: " + wifiManager.isWifiEnabled());

        // Go through all host addresses to scan active IP addresses
        try {
            for (int i = 0; i < 256; i++) {
                StringBuilder sb = new StringBuilder();

                // Build IP address to ping
                sb.append(ip & 0xFF);
                sb.append(".");
                sb.append((ip >> 8) & 0xFF);
                sb.append(".");
                sb.append((ip >> 16) & 0xFF);
                sb.append(".");
                sb.append(i);

                String ipAddress = sb.toString();

                // Check to see if IP address is reachable
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                if (inetAddress.isReachable(100)) {
                    Log.d("WIFI TEST", inetAddress.toString() + " is reachable");
                    Log.d("WIFI TEST", "Name: " + inetAddress.getHostName());
                }
            }
        }catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}