package org.example;

import org.eclipse.paho.client.mqttv3.*;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FirstClient {
    private static boolean log = true;
    private static boolean location = true;
    private static IMqttClient client;

    static {
        try {
            client = mqttClient();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        sendLocationInfo();


        List<Thread> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            list.add(sendLog());
            list.add(sendLocationInfo());
        }

        for(Thread thread : list) {
            thread.start();
        }

        consumeUrgentCommand();

    }
    public static Thread sendLog() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (log) {
                    try {
                        if (!client.isConnected()) {
                            throw new ConnectException("Mqtt server is not running");
                        }
                        MqttMessage msg = new MqttMessage();
                        msg.setPayload(StaticData.prepareLogData());
                        msg.setQos(0);
                        msg.setRetained(true);
                        client.publish("log", msg);
                        System.out.println("---> Log Thread is working");
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        return thread;
    }
    private static Thread sendLocationInfo() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (location) {
                    try {
                        if (!client.isConnected()) {
                            throw new ConnectException("Mqtt server is not running");
                        }
                        MqttMessage msg = new MqttMessage();
                        msg.setQos(0);
                        msg.setPayload(StaticData.prepareLocationData());
                        msg.setRetained(true);
                        client.publish("location", msg);
                        System.out.println("---> Location Thread is working");
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
        return thread;
    }
    private static void consumeUrgentCommand() {
        System.out.println("System is working.........");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.subscribe(String.format("command:  %s", StaticData.electronicCardUUID), ((topic, message) -> {
                        if (message.toString().equals("RUN")) {
                            System.out.println("--->>>>>>>>>>>>>>> ENGINE IS RUNNING");
                            System.exit(0);
                        } else if (message.toString().equals("NOTHING")) {
                            System.out.println("--->>>>>>>>>>>>>>>> NOTHING");
                        }
                    }));
                } catch (MqttException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

    }
    private static IMqttClient mqttClient() throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        MqttConnectOptions options;
        IMqttClient publisher = new MqttClient("tcp://localhost:1883", publisherId);
        options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setPassword("admin".toCharArray());
        options.setUserName("admin");
        publisher.connect(options);
        return publisher;
    }
}