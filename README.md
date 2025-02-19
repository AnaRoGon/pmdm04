# Introduccion
Para la actualización de la aplicación se ha incluidouna guía interactiva que ayudará al usuario a conocer mejor su funcionamiento. 
Se han utilizado animaciones, videos y sonidos relacionados con la temática de Spyro y se han añadido dos EasterEgg para hacerla más interesante. 

# Características principales

* Guía interactiva con animaciones y efectos sonoros.

* Diseños inspirados en el mundo de Spyro.

* Dos Easter Eggs ocultos para los jugadores más atentos.

# Tecnologías utilizadas

* **Drawables personalizados**: Se han creado nuevos diseños **Shape** y **Selector** para personalizar los botones y bocadillos de la guía.
* **Animation Drawable**: Se ha utilizado la clase **AnimationDrawable** para insertar animaciones relacionadas con la temática de Spyro (como el fuego y Spyro volando al comienzo de la guía). 
* **Transiciones**: Se han implementado transiciones desde el navegador de la aplicación y haciendo uso de la clase **Transition** y el diseño de xml de tipo **TransitionSet** personalizados para las transiciones del inicio y el final de la guía.
* **ObjectAnimator**: Se ha utilizado la clase **ObjectAnimator** para insertar animaciones que afectan a los bocadillos de texto, botones y menú de navegación.
* **AnimatorSet**: Se ha utilizado la clase **AnimatorSet**, aprovechando que nos permite crear un set de animaciones y ejecutarlas a la vez para conseguir un efecto más organizado y simple.
* **Media Player**: Se ha utilizado **MediaPlayer** para insertar sonidos y videos temáticos en la aplicación. Además de la etiqueta xml **VideoView**. 
* **Canvas**: Se ha utilizado la clase **Canvas**, junto a **Paint** para crear una clase personalizada que nos permite simular una animación de fuego saliendo de la boca de Spyro en uno de los EasterEgg implementados. 

# Instrucciones de uso 

El uso de la guía interactiva es automático. Se iniciará por defecto la primera vez que se ejecute la aplicación. 

Si el usuario selecciona el botón de saltar guía o la finaliza por completo la guía no volverá a aparecer. 

Para activar los EasterEgg: 

1. **Clicar 4 veces en el RecyclerView de las Gemas**. El video redirigirá a la pantalla de coleccionables al finalizar o dejará de reproducirse si se clica sobre la pantalla. 
2. **Mantener pulsado en el RecyclerView de Spyro** en la pantalla de personajes. Si se hace clic se parará la animación y el sonido. 

# Conclusiones del desarrollador 

Incluir elementos multimedia para la realización de esta guía interactiva ha significado un reto en algunos momentos, especialmente con el diseño de las transiciones. 
Por razones aún no del todo claras, estas no siempre se implementan correctamente y su funcionamiento es inconsistente. Se sospecha que el rendimiento del ordenador personal ha podido influir en este problema.
Trabajar con multimedia en Android ha dejado en evidencia la importancia de la gestión de recursos.
La liberación de memoria y la optimización del código han sido aspectos clave para evitar problemas de rendimiento, algo que se ha intentado controlar de la mejor manera posible dentro del desarrollo.

Finalmente, el uso de la clase Canvas junto a Paint para la **creación de la animación** de fuego **ha sido todo un desafío** y se han tenido que realizar muchas pruebas e intentos hasta dar con una solución que se considerase aceptable.

Aunque me he decantado por el intercambio aleatorio en el color de los círculos "pintados" para simular la llamarada, se hicieron pruebas en las que modificando el radio, se podía conseguir que la llamarada saliese para, acto seguido, desaparecer.
Sin embargo me ha parecido mas interesante que se mantenga e implementar un método que permita que sea el usuario el que decida cuando desaparezce la llamarada. 

# Capturas de pantalla


![01Captura](https://github.com/user-attachments/assets/75c569b2-c6d3-4299-836a-d0d7bf9652d5)


![02Captura](https://github.com/user-attachments/assets/bdc40506-f614-47cc-8523-96416c2753bf)

![03Captura](https://github.com/user-attachments/assets/60b12db9-8b1b-48f3-81cc-85b180649893)

![04Captura](https://github.com/user-attachments/assets/874d7f82-3db6-40cb-b71d-fa11d5f7301c)



