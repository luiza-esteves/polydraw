package math;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2DTest {
    @Test
    void shouldAddTwoVectors() {
        Vector2D a = new Vector2D(2, 3);
        Vector2D b = new Vector2D(4, 5);

        Vector2D result = a.add(b);

        assertEquals(6, result.getX());
        assertEquals(8, result.getY());
    }

    @Test
    void shouldSubtractTwoVectors(){
        Vector2D a = new Vector2D(2, 3);
        Vector2D b = new Vector2D(4, 5);
        Vector2D result = b.subtract(a);
        assertEquals(2, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    void shouldMultiplyScalar(){
        double scalar =8.0;
        Vector2D a = new Vector2D(2, 3);
        Vector2D result = a.multiply(scalar);
        assertEquals(16, result.getX());
        assertEquals(24, result.getY());
    }

    @Test
    void shouldCalculateLength() {
        Vector2D a = new Vector2D(2, 3);

        double result = a.length();

        assertEquals(Math.sqrt(13), result, 0.0001);
    }

    @Test
    void shouldNormalize(){

        Vector2D a = new Vector2D(60, 80);
        Vector2D result = a.normalize();
        assertEquals(0.6, result.getX(), 0.0001);
        assertEquals(0.8, result.getY(), 0.0001);
        assertEquals(1.0, result.length(), 0.0001);
    }

    @Test
    void shouldNotNormalizeZeroVector() {
        Vector2D vector = new Vector2D(0, 0);

        assertThrows(
                IllegalStateException.class,
                vector::normalize
        );
    }
}
