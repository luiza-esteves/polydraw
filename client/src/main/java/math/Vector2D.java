package math;

public class Vector2D {

    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2D add(Vector2D b) {
        return new Vector2D(this.x + b.x, this.y + b.y);
    }

    public Vector2D subtract(Vector2D b) {
        return new Vector2D(this.x - b.x, this.y - b.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public double length() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public Vector2D normalize() {
        double length = this.length();

        if (length == 0) {
            throw new IllegalStateException("Cannot normalize a zero vector");
        }

        return new Vector2D(
                this.x / length,
                this.y / length
        );
    }


}