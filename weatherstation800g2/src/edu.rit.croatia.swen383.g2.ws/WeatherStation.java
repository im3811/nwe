package edu.rit.croatia.swen383.g2.ws;
import java.util.EnumMap;
import edu.rit.croatia.swen383.g2.ws.util.MeasurementUnit;
import edu.rit.croatia.swen383.g2.ws.observer.Subject;
import java.util.Random;

public class WeatherStation extends Subject {
    private final EnumMap<MeasurementUnit, Double> readingMap;
    private static final long PERIOD = 1000;

    public WeatherStation() {
        this.readingMap = new EnumMap<>(MeasurementUnit.class);
    }

    private void getSensorReadings() {
        // Simulate readings directly without sensors
        Random rand = new Random();
        
        // Temperature (around 20°C / 293.15K / 68°F)
        readingMap.put(MeasurementUnit.CELSIUS, 20.0 + rand.nextDouble(-5, 5));
        readingMap.put(MeasurementUnit.KELVIN, 293.15 + rand.nextDouble(-5, 5));
        readingMap.put(MeasurementUnit.FAHRENHEIT, 68.0 + rand.nextDouble(-5, 5));
        
        // Pressure (around 1013.25 mbar / 29.92 inHg)
        readingMap.put(MeasurementUnit.MBAR, 1013.25 + rand.nextDouble(-10, 10));
        readingMap.put(MeasurementUnit.INHG, 29.92 + rand.nextDouble(-0.5, 0.5));
    }

    public double getReading(MeasurementUnit unit) {
        return readingMap.get(unit);
    }

    public void run() {
        while (true) {
            try {
                getSensorReadings();
                notifyObservers();
                Thread.sleep(PERIOD);
            } catch (InterruptedException e) {
                System.err.println("Weather station monitoring interrupted!");
                break;
            }
        }
    }
}