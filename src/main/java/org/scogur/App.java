package org.scogur;

import org.json.JSONArray;
import org.json.JSONObject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import static java.lang.Math.abs;

@Command(name = "start")
public class App implements Runnable {
    @Parameters(index = "0", description = "input tickets.json file")
    private File file;
    @Override
    public void run() {
        System.out.println("Started " + file);
        HashMap<String, Long> minimalFlightTime = new HashMap<>();
        float averagePrice = 0;
        int amount = 0;
        ArrayList<Integer> prices = new ArrayList<>();
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
                    if (minimalFlightTime.isEmpty() | !minimalFlightTime.containsKey(jo.get("carrier").toString())){
                        minimalFlightTime.put(jo.get("carrier").toString(), flightTime);
                    } else if ( flightTime < minimalFlightTime.get(jo.get("carrier").toString())){
                        minimalFlightTime.put(jo.get("carrier").toString(), flightTime);
                    }
                    prices.add(jo.getInt("price"));
                    averagePrice += (jo.getFloat("price"));
                    amount ++;
                }
            }
            averagePrice /= amount;
            Collections.sort(prices);

            if (prices.size() % 2  == 0){
                medianPrice = (float) (prices.get((prices.size()-1)/2) + prices.get((prices.size()-1)/2 +1))/2;
            } else
                medianPrice = (float) (prices.get((prices.size()-1)/2+1));

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