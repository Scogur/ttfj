package org.scogur;

import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@Command(name = "start")
public class App implements Runnable {
    @Parameters(index = "0", description = "input tickets.json file")
    private File file;
    @SuppressWarnings("ReassignedVariable")
    @Override
    public void run() {
        System.out.println("Started " + file);
        long minimalFlightTime = 0;
        float averagePrice = 0;
        float medianPrice = 0;
        try {
            JSONObject o = new JSONObject((Files.readString(file.toPath())));
            JSONArray tickets = o.getJSONArray("tickets");

            List<Integer> reqPrice = new ArrayList<>();
            boolean first = true;
            for (int i = 0; i<tickets.length(); i++){
                JSONObject jo = new JSONObject(o.getJSONArray("tickets").get(i).toString());
                if (((jo.get("origin").toString().equals("VVO")) | (jo.get("origin").toString().equals("TLV"))) & ((jo.get("destination").toString().equals("TLV")) | (jo.get("destination").toString().equals("VVO")))){
                    MyDate depdate = new MyDate(jo.get("departure_date").toString());
                    depdate.setTime(jo.get("departure_time").toString());
                    MyDate ardate = new MyDate(jo.get("arrival_date").toString());
                    ardate.setTime(jo.get("arrival_time").toString());
                    long flightTime = MyDate.subtraction(depdate, ardate);
                    if (first){
                        minimalFlightTime = flightTime;
                        first = false;
                    } else if (flightTime < minimalFlightTime) minimalFlightTime = flightTime;
                    averagePrice += (jo.getFloat("price"));
                    reqPrice.add(jo.getInt("price"));

                }
            }
            averagePrice /= tickets.length();

            if (reqPrice.size() % 2 == 0){
                medianPrice += reqPrice.get(reqPrice.size() / 2);
                medianPrice += reqPrice.get((reqPrice.size() / 2) + 1);
                medianPrice /=2;
            } else {
                medianPrice = reqPrice.get((reqPrice.size() / 2) + 1);
            }

            System.out.println("Minimal flight time: " + minimalFlightTime + " minutes" +
                    "\nDifference between average price and median price: " + abs(averagePrice - medianPrice));
            System.out.println("GitHub repo: https://github.com/Scogur/ttfj");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new App()).execute(args));
    }
}