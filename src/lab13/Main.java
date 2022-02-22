package lab13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String path = "/path/to/lab13/examples/";
        CSVReader csvReader = null;

        List<Przystanek> przystanki = new ArrayList<>();

        try {
            csvReader = new CSVReader(path + "przystanki.csv", ";", true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (csvReader != null && csvReader.next()) {
            Przystanek przystanek = new Przystanek();
            try {
                przystanek.id = csvReader.getInt("Lp");
                przystanek.latitude = csvReader.getDouble("Lat");
                przystanek.longitude = csvReader.getDouble("Lon");
            } catch (Exception e) {
                e.printStackTrace();
            }
            przystanek.nazwa = csvReader.get("Nazwa");
            przystanek.droga = csvReader.get("Droga");
            przystanek.kilometraz = Double.parseDouble(csvReader.get("Kilometraż").replace('+', '.'));

            przystanki.add(przystanek);
        }

        System.out.println("--- Zadanie A ---");
        for (Przystanek p : przystanki) {
            if (p.droga.charAt(0) != 'P') continue;
            System.out.printf("%s - %d - %s\n", p.droga, p.id, p.nazwa);
        }
        System.out.println();

        System.out.println("--- Zadanie B ---");
        BoundingBox bbB = new BoundingBox();
        bbB.xmin = 50.54;
        bbB.xmax = 50.62;
        bbB.ymin = 21.63;
        bbB.ymax = 21.73;
        List<Przystanek> resB = new ArrayList<>();
        for (Przystanek p : przystanki) {
            if (!bbB.contains(p.latitude, p.longitude)) continue;
            resB.add(p);
        }
        resB.sort((a, b) -> a.nazwa.compareToIgnoreCase(b.nazwa));
        for (Przystanek p : resB) {
            System.out.printf("%s - %d - %f, %f\n", p.nazwa, p.id, p.latitude, p.longitude);
        }
        System.out.println();

        System.out.println("--- Zadanie C ---");
        List<Przystanek> resC = new ArrayList<>();
        for (Przystanek p : przystanki) {
            if (!p.nazwa.contains("Warszawska")) continue;
            resC.add(p);
        }
        resC.sort(Comparator.comparingDouble(a -> a.kilometraz));
        for (Przystanek p : resC) {
            System.out.printf("%.3fkm - %d - %s\n",  p.kilometraz, p.id,p.nazwa);
        }
        System.out.println();

        System.out.println("--- Zadanie D ---");
        BoundingBox bbD = new BoundingBox();
        for (Przystanek p : przystanki) {
            bbD.addPoint(p.latitude, p.longitude);
        }
        System.out.println("Współrzędne prostokąta:");
        System.out.printf("[%f %f]\n", bbD.xmin, bbD.ymin);
        System.out.printf("[%f %f]\n", bbD.xmin, bbD.ymax);
        System.out.printf("[%f %f]\n", bbD.xmax, bbD.ymax);
        System.out.printf("[%f %f]\n", bbD.xmax, bbD.ymin);
    }
}
