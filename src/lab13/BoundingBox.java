package lab13;

public class BoundingBox {
    double xmin = Double.NaN;
    double ymin = Double.NaN;
    double xmax = Double.NaN;
    double ymax = Double.NaN;

    /**
     * Powiększa BB tak, aby zawierał punkt (x,y)
     *
     * @param x - współrzędna x
     * @param y - współrzędna y
     */
    void addPoint(double x, double y) {
        if (Double.isNaN(this.xmin)) {
            this.xmin = x;
        }

        if (Double.isNaN(this.xmax)) {
            this.xmax = x;
        }

        if (Double.isNaN(this.ymin)) {
            this.ymin = y;
        }

        if (Double.isNaN(this.ymax)) {
            this.ymax = y;
        }

        if (x > this.xmax) {
            this.xmax = x;
        } else {
            this.xmin = x;
        }

        if (y > this.ymax) {
            this.ymax = y;
        } else {
            this.ymin = y;
        }
    }

    /**
     * Sprawdza, czy BB zawiera punkt (x,y)
     *
     * @param x - współrzędna x
     * @param y - współrzędna y
     * @return true|false
     */
    boolean contains(double x, double y) {
        return x >= this.xmin && x <= this.xmax && y >= this.ymin && y <= this.ymax;
    }

    /**
     * Sprawdza czy dany BB zawiera bb
     *
     * @param bb - bounding box
     * @return true|false
     */
    boolean contains(BoundingBox bb) {
        return bb.xmin >= this.xmin && bb.xmax <= this.xmax && bb.ymin >= this.ymin && bb.ymax <= this.ymax;
    }

    /**
     * Sprawdza, czy dany BB przecina się z bb
     *
     * @param bb - bounding box
     * @return true|false
     */
    boolean intersects(BoundingBox bb) {
        return !(bb.xmin > this.xmax || bb.ymin > this.ymax || this.xmin > bb.xmin || this.ymin > bb.ymax);
    }

    /**
     * Powiększa rozmiary tak, aby zawierał bb oraz poprzednią wersję this
     *
     * @param bb - bounding box
     * @return powiększony Bounding Box
     */
    BoundingBox add(BoundingBox bb) {
        this.xmin = Math.min(this.xmin, bb.xmin);
        this.ymin = Math.min(this.ymin, bb.ymin);
        this.xmax = Math.max(this.xmax, bb.xmax);
        this.ymax = Math.max(this.ymax, bb.ymax);

        return this;
    }

    /**
     * Sprawdza czy BB jest pusty
     *
     * @return true|false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean isEmpty() {
        return Double.isNaN(this.xmin) || Double.isNaN(this.ymin) || Double.isNaN(this.xmax) || Double.isNaN(this.ymax);
    }

    /**
     * Oblicza i zwraca współrzędną x środka
     *
     * @return if !isEmpty() współrzędna x środka else wyrzuca wyjątek
     * (sam dobierz typ)
     */
    double getCenterX() {
        if (!this.isEmpty()) {
            return (this.xmin + this.xmax) / 2;
        } else {
            throw new RuntimeException("BoundingBox is empty!");
        }
    }

    /**
     * Oblicza i zwraca współrzędną y środka
     *
     * @return if !isEmpty() współrzędna y środka else wyrzuca wyjątek
     * (sam dobierz typ)
     */
    double getCenterY() {
        if (!this.isEmpty()) {
            return (this.ymin + this.ymax) / 2;
        } else {
            throw new RuntimeException("BoundingBox is empty!");
        }
    }

    /**
     * Oblicza odległość pomiędzy środkami this bounding box oraz bbx
     *
     * @param bb prostokąt, do którego liczona jest odległość
     * @return if !isEmpty odległość, else wyrzuca wyjątek lub zwraca maksymalną możliwą wartość double
     * Ze względu na to, że są to współrzędne geograficzne, zamiast odległości użyj wzoru haversine
     * (ang. haversine formula)
     * <p>
     * Gotowy kod można znaleźć w Internecie...
     */
    public double distanceTo(BoundingBox bb) {
        double R = 6371;

        double dLat = Math.toRadians(bb.getCenterX() - this.getCenterX());
        double dLon = Math.toRadians(bb.getCenterY() - this.getCenterY());
        double lat1 = Math.toRadians(this.getCenterX());
        double lat2 = Math.toRadians(bb.getCenterX());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}
