package org.example;

import org.json.JSONObject;

import java.util.Date;


public final class StaticData {
    public final static String electronicCardUUID = "77b21c42-85cb-46d5-9a7c-61aa0dc588b4";

    private StaticData() {

    }

    public static byte[] prepareLocationData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("id", 1);
        jsonObject.append("latitude", 38.415667);
        jsonObject.append("longitude", 27.056400);
        return jsonObject.toString().getBytes();
    }

    public static byte[] prepareLogData() {
        JSONObject jsonObject = new JSONObject();
        String[] keys = {"id", "modelNo", "sensorType", "detectionGas", "concentration", "loopVoltage", "heaterVoltage", "loadResistance", "heaterConsumption", "sensingResistance", "sensitivity", "slope", "temHumidity", "standardTestCircuit", "preheatTime"};
        String[] values = {electronicCardUUID, "M001", "MQ-2", "LPG/Propane/Butane/Hydrogen/Smoke", "0-1000ppm", "5V DC ±0.1V DC", "5V DC ±0.1V DC", "20kΩ (Variable)", "<800mW (5V DC)", "<10kΩ (1000ppm LPG)", ">3% (500ppm LPG)", "-0.6%/°C (500ppm LPG)", "-20°C to 50°C, 95%RH max.", "Rl=20kΩ, Vc=5V DC ±0.1V DC, Vh=5V DC ±0.1V DC, Preheat time: Over 48 hours.", new Date().toString()};
        for (int i = 0; i < keys.length; i++) {
            jsonObject.append(keys[i], values[i]);
        }
        return jsonObject.toString().getBytes();
    }

    public static byte[] prepareUrgentData() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("id", 1);
        jsonObject.append("urgentLevel", UrgentLevel.HIGH.name());
        return jsonObject.toString().getBytes();
    }


}
