package dam.pmdm.spyrothedragon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase FlamesCanvasView.
 * Esta clase utiliza Canvas y Paint para simular una animación de fuego.
 * Para ello crea una lista de círculos que van cambiando de color y repintandose
 * en la pantalla.
 */

public class FlamesCanvasView extends View {
    //Variable de la clase
    private Paint paint;
    private List<Circle> circles = new ArrayList<>();

    /**
     * Constructor de la clase FlamesCanvasView.
     *
     * @param context Contexto de la aplicación.
     * @param attrs   Atributos opcionales de la vista.
     */
    public FlamesCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        //Color rojo por defecto
        paint.setColor(Color.RED);
        //Relleno
        paint.setStyle(Paint.Style.FILL);
        //Efecto de suavizado de bordes que mejora la calidad de las líneas
        paint.setAntiAlias(true);
    }


    /**
     * Metodo encargado de dibujar los círculos en la vista.
     * Se sobrescriben los colores de los círculos creados para dar la sensación de fuego en movimiento.
     *
     * @param canvas Objeto Canvas donde se dibujan los circulos.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Primero se dibujan los circulos en la pantalla
        for (Circle circle : circles) {
            paint.setColor(circle.color);
            canvas.drawCircle(circle.centerX, circle.centerY, circle.radius, paint);
        }

        //Luego se repintan los colores de los circulos creados para crear un efecto de animación de fuego
        for (Circle circle : circles) {
            circle.setColor(getRandomColor());
            //circle.radius -= 1; Si modificamos también el radio la llamarada se va
            invalidate(); //Se redibuja la vista
        }

    }

    /**
     * Genera un color aleatorio dentro de una gama de colores cálidos (rojo, naranja y amarillo).
     *
     * @return un color aleatorio.
     */
    private int getRandomColor() {
        //Almacenamos una lista de colores cálidos para el fuego
        int[] colors = {
                Color.rgb(255, 0, 0),    // Rojo puro
                Color.rgb(200, 0, 0),    // Rojo oscuro
                Color.rgb(255, 50, 0),   // Naranja saturado
                Color.rgb(255, 165, 0),  // Naranja clásico
                Color.rgb(255, 120, 0),  // Naranja suave
                Color.rgb(255, 165, 50), // Amarillo-naranja
                Color.rgb(255, 200, 0),  // Amarillo fuerte
                Color.rgb(255, 255, 0),  // Amarillo puro
                Color.rgb(255, 215, 0)   // Amarillo dorado
        };
        //Obtenemos un color aleatorio de la lista
        return colors[(int) (Math.random() * colors.length)];
    }

    /**
     * Clase interna que representa un círculo en la vista.
     */
    private static class Circle {
        //Variables de la clase
        float centerX;
        float centerY;
        float radius;
        int color;

        /**
         * Constructor de la clase Circle.
         *
         * @param centerX coordenada X del centro del círculo.
         * @param centerY coordenada Y del centro del círculo.
         * @param radius  radio del círculo.
         * @param color   color del círculo.
         */
        Circle(float centerX, float centerY, float radius, int color) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.color = color;
        }

        //Metodo setter de la clase Circle para poder cambiar el color del circulo
        public void setColor(int color) {
            this.color = color;
        }
    }

    /**
     * Metodo para iniciar la animación en una ubicación específica.
     *
     * @param centerX coordenada X del centro del primer círculo.
     * @param centerY coordenada Y del centro del primer círculo.
     * @param radius  radio del primer círculo.
     */
    public void animationLocalitation(float centerX, float centerY, float radius) {
        //El primer circulo se crea en base a la ubicación y radio recibidos
        Circle circle = new Circle(centerX, centerY, radius, getRandomColor());
        //Se añade a la lista de círculos
        circles.add(circle);
        //Creamos 50 circulos más para simular el fuego
        for (int i = 0; i < 50; i++) {
            Circle circle2 = new Circle(centerX -= 5, centerY -= 5, radius -= 1, getRandomColor());
            circles.add(circle2);
        }
        invalidate();//Se redibuja la vista
    }

    /**
     * Metodo para eliminar todos los circulos creados.
     */
    public void removeCircles() {
        //Borramos todos los circulos de la lista
        circles.clear();
        invalidate(); //Se redibuja la vista
    }

}
