package dam.pmdm.spyrothedragon;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding;
import dam.pmdm.spyrothedragon.databinding.GuideBinding;
import dam.pmdm.spyrothedragon.databinding.GuideInfoButtonBinding;
import dam.pmdm.spyrothedragon.databinding.Guidetab1Binding;
import dam.pmdm.spyrothedragon.databinding.Guidetab2Binding;
import dam.pmdm.spyrothedragon.databinding.Guidetab3Binding;
import dam.pmdm.spyrothedragon.databinding.SummaryBinding;
import dam.pmdm.spyrothedragon.databinding.VideoBinding;

/**
 * Esta clase implementa la lógica principal de la aplicación.
 * Se trata de la Activity principal que se nos abrirá con la sesión iniciada y contendrá
 * los fragmentos de la aplicación.
 * DAM. Curso 2024-2025
 * Módulo: PMDM
 * Unidad 4: Multimedia.
 * Título de la tarea: Aplicación Android de Pokémon.
 *
 * @author Ana Rodríguez González
 * @version 1.0 Fecha: 22-01-2025
 */
public class MainActivity extends AppCompatActivity {
    //Variables de la actividad
    private ActivityMainBinding binding;
    NavController navController = null;
    private GuideBinding guideBinding;
    private Guidetab1Binding guideTab1Binding;
    private Guidetab2Binding guideTab2Binding;
    private Guidetab3Binding guideTab3Binding;
    private SummaryBinding summaryBinding;
    private VideoBinding videoBinding;
    private GuideInfoButtonBinding guideInfoButtonBinding;
    private MediaPlayer mediaPlayer;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //Se inicializan los layout de la guía
        guideBinding = binding.guideLayout;
        guideTab1Binding = binding.guideLayout1;
        guideTab2Binding = binding.guideLayout2;
        guideTab3Binding = binding.guideLayout3;
        guideInfoButtonBinding = binding.guideLayout4;
        summaryBinding = binding.summaryLayout;
        videoBinding = binding.videoLayout;
        setContentView(binding.getRoot());

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.navView, navController);
            NavigationUI.setupActionBarWithNavController(this, navController);
        }

        binding.navView.setOnItemSelectedListener(this::selectedBottomMenu);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_characters ||
                    destination.getId() == R.id.navigation_worlds ||
                    destination.getId() == R.id.navigation_collectibles) {
                // Para las pantallas de los tabs, no queremos que aparezca la flecha de atrás
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            } else {
                // Si se navega a una pantalla donde se desea mostrar la flecha de atrás, habilítala
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        });

        //Recuperamos el sharedPreferences
        sharedPreferences = getSharedPreferences("guide_prefs", MODE_PRIVATE);
        //Recuperamos el valor de la guía que por defecto será false
        boolean guiaComenzada = sharedPreferences.getBoolean("guide_started", false);
        //Si no se ha completado la guía antes se inicializa
        if (!guiaComenzada) {
            initializeGuide();
            //Al pulsar el botón de continuar se inicia la primera parte de la guía
            guideBinding.guideButton.setOnClickListener(this::startGuide);
        }
    }

    //Con esta funcion se inicializa la pantalla principal de la guía
    private void initializeGuide() {
        //Al iniciar la guia se reproduce un sonido de inicio con el tema principal de Spyro
        mediaPlayer = MediaPlayer.create(this, R.raw.main_theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //Se hace visible el layout de la pantalla inicial
        guideBinding.guideLayout.setVisibility(View.VISIBLE);
        //Y se inicializan las animaciones para esta pantalla
        initializeAnimations();
    }


    //Con esta función se hace el paso a la siguiente pantalla de la guía
    private void startGuide(View view) {
        //Creamos la transición de la pantalla principal de la guia
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.transition_guide);
        //Aplicar transicion
        TransitionManager.beginDelayedTransition(guideBinding.guideLayout, transition);
        //Cerramos la pantalla principal de la guía
        guideBinding.guideLayout.setVisibility(View.GONE);
        //Se reproduce un sonido al haber pulsado el botón para comenzar
        android.media.MediaPlayer mediaPlayer = android.media.MediaPlayer.create(this, R.raw.clocktic);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Iniciamos la pantalla para la primera pestaña de la guía, la de los personajes
        initializeGuideTab1();
    }

    //Con esta función se muestra la primera pantalla de la guía, en la que se explica el fragmento de los personajes
    private void initializeGuideTab1() {
        //Al comenzar la primera pantalla de la guía de para el tema principal
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
        //Si el usuario quiere puede saltarse la guia
        guideTab1Binding.buttonSkipGuide.setOnClickListener(this::skipGuide);
        //Al hacer clic sobre el botón para continuar se inicia la segunda pantalla de la guía
        guideTab1Binding.continueButton.setOnClickListener(this::initializeGuideTab2);
        //Hacemos visible la pantalla de la guia actual
        guideTab1Binding.guideTabLayout1.setVisibility(View.VISIBLE);

        //Con un efecto de animación aparece el bocadillo con el texto y la flecha
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(guideTab1Binding.dialog1, "alpha", 0f, 1f);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(guideTab1Binding.flecha, "alpha", 0f, 1f);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(guideTab1Binding.continueButton, "alpha", 0f, 1f);
        ObjectAnimator fadeIn4 = ObjectAnimator.ofFloat(guideTab1Binding.buttonSkipGuide, "alpha", 0f, 1f);

        //Usamos AnimatorSet para agrupar las animaciones y lanzarlas a la vez
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.playTogether(fadeIn, fadeIn2, fadeIn3, fadeIn4);
        animatorSet.start();

        //Animamos el botón de los personajes para llamar la atención del usuario
        animateButtonPulse(R.id.nav_characters);
    }

    //Con esta función se muestra la segunda pantalla de la guía, en la que se explica el fragmento de los mundos
    private void initializeGuideTab2(View view) {
        //Al seguir la guía se reproduce un sonido de avance
        mediaPlayer = MediaPlayer.create(this, R.raw.clocktic);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Si el usuario quiere puede saltarse la guia
        guideTab2Binding.buttonSkipGuide.setOnClickListener(this::skipGuide);
        //Al hacer click sobre la pantalla continua la guia
        guideTab2Binding.continueButton.setOnClickListener(this::initializeGuideTab3);
        //Nos diriguimos a la pestaña de los mundos
        navController.navigate(R.id.action_navigation_characters_to_navigation_worlds2);
        //Ocultamos la pantalla anterior de la guia
        guideTab1Binding.guideTabLayout1.setVisibility(View.GONE);
        //Mostramos la actual pantalla de la guia
        guideTab2Binding.guideTabLayout2.setVisibility(View.VISIBLE);

        //Con un efecto de animación aparece el bocadillo con el texto y la flecha
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(guideTab2Binding.dialog1, "alpha", 0f, 1f);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(guideTab2Binding.flecha, "alpha", 0f, 1f);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(guideTab2Binding.continueButton, "alpha", 0f, 1f);
        ObjectAnimator fadeIn4 = ObjectAnimator.ofFloat(guideTab2Binding.buttonSkipGuide, "alpha", 0f, 1f);

        //Usamos AnimatorSet para agrupar las animaciones y lanzarlas a la vez
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.playTogether(fadeIn, fadeIn2, fadeIn3, fadeIn4);
        animatorSet.start();

        //Animamos el botón de los mundos para llamar la atención del usuario
        animateButtonPulse(R.id.nav_worlds);
    }

    //Con esta función se muestra la tercera pantalla de la guía, en la que se explica el fragmento de los coleccionables
    private void initializeGuideTab3(View view) {
        // Al seguir la guía se reproduce un sonido de avance
        mediaPlayer = MediaPlayer.create(this, R.raw.clocktic);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Si el usuario quiere puede saltarse la guia
        guideTab3Binding.buttonSkipGuide.setOnClickListener(this::skipGuide);
        //Agregar listener para siguiente pantalla de la guia
        guideTab3Binding.continueButton.setOnClickListener(this::initializeGuideInfoButton);
        //Nos diriguimos a la pestaña de los collectibles usando la accion de navegación
        navController.navigate(R.id.action_navigation_worlds_to_navigation_collectibles2);
        //Ocultamos la pantalla anterior de la guia
        guideTab2Binding.guideTabLayout2.setVisibility(View.GONE);
        //Mostramos la pantalla actual de la guia
        guideTab3Binding.guideTabLayout3.setVisibility(View.VISIBLE);

        //Con un efecto de animación aparece el bocadillo con el texto y la flecha
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(guideTab3Binding.dialog1, "alpha", 0f, 1f);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(guideTab3Binding.flecha, "alpha", 0f, 1f);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(guideTab3Binding.continueButton, "alpha", 0f, 1f);
        ObjectAnimator fadeIn4 = ObjectAnimator.ofFloat(guideTab3Binding.buttonSkipGuide, "alpha", 0f, 1f);

        //Usamos AnimatorSet para agrupar las animaciones y lanzarlas a la vez
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.playTogether(fadeIn, fadeIn2, fadeIn3, fadeIn4);
        animatorSet.start();

        //Animamos el botón de los collectibles
        animateButtonPulse(R.id.nav_collectibles);
    }

    //Con esta función se muestra la penúltima pantalla de la guía, en la que se explica el botón de información de la app
    private void initializeGuideInfoButton(View view) {
        // Al seguir la guía se reproduce un sonido de avance
        mediaPlayer = MediaPlayer.create(this, R.raw.clocktic);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Si el usuario quiere puede saltarse la guia
        guideInfoButtonBinding.buttonSkipGuide.setOnClickListener(this::skipGuide);
        //Agregar listener para siguiente pantalla de la guia
        guideInfoButtonBinding.continueButton.setOnClickListener(this::initializeSummary);
        //Ocultamos la pantalla anterior
        guideTab3Binding.guideTabLayout3.setVisibility(View.GONE);
        //Mostramos la pantalla actual de la guia
        guideInfoButtonBinding.guideInfoButton.setVisibility(View.VISIBLE);

        //Con un efecto de animación aparece el bocadillo con el texto y la flecha
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(guideInfoButtonBinding.dialog1, "alpha", 0f, 1f);
        ObjectAnimator fadeIn2 = ObjectAnimator.ofFloat(guideInfoButtonBinding.flecha, "alpha", 0f, 1f);
        ObjectAnimator fadeIn3 = ObjectAnimator.ofFloat(guideInfoButtonBinding.continueButton, "alpha", 0f, 1f);
        ObjectAnimator fadeIn4 = ObjectAnimator.ofFloat(guideInfoButtonBinding.buttonSkipGuide, "alpha", 0f, 1f);

        //Usamos AnimatorSet para agrupar las animaciones y lanzarlas a la vez
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.playTogether(fadeIn, fadeIn2, fadeIn3, fadeIn4);
        animatorSet.start();
    }


    //Con esta funcion se inicializa la pantalla de resumen final de la guía
    private void initializeSummary(View view) {
        //Al finalizar la guía se reproduce un sonido de éxito
        mediaPlayer = MediaPlayer.create(this, R.raw.finish);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Salimos de la anterior pantalla
        guideInfoButtonBinding.guideInfoButton.setVisibility(View.GONE);
        //Se muestra la pantalla de resumen
        summaryBinding.summaryLayout.setVisibility(View.VISIBLE);
        //Al pulsar en el botón se finaliza la guía
        summaryBinding.button.setOnClickListener(this::finish);
    }

    //Con esta funcion se finaliza la guía
    private void finish(View view) {
        //Al finalizar la guía se reproduce un sonido de éxito
        mediaPlayer = MediaPlayer.create(this, R.raw.boomsmall);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Guardamos en el sharedPreferences que se ha completado la guía
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("guide_started", true);
        editor.apply();
        //Salimos de la guía con un efecto de transición
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.final_transition);
        TransitionManager.beginDelayedTransition(summaryBinding.summaryLayout, transition);
        //Se oculta la pantalla
        summaryBinding.summaryLayout.setVisibility(View.GONE);
    }


    //Con esta función el usuario puede saltarse la guía en cualquier momento
    private void skipGuide(View view) {
        //Si se salta la guia también dejará de mostrarse al iniciarse la app en el futuro
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("guide_started", true);
        editor.apply();
        //Si el usuario se salta la guia se reproduce un sonido diferente
        mediaPlayer = MediaPlayer.create(this, R.raw.boomsmall);
        mediaPlayer.start();
        //Añadimos el listener para que al completarse el sonido se libere
        mediaPlayer.setOnCompletionListener(mp -> mediaPlayer.release());
        //Se ocultan todas las pantallas de la guía
        guideTab1Binding.guideTabLayout1.setVisibility(View.GONE);
        guideTab2Binding.guideTabLayout2.setVisibility(View.GONE);
        guideTab3Binding.guideTabLayout3.setVisibility(View.GONE);
        guideInfoButtonBinding.guideInfoButton.setVisibility(View.GONE);
    }

    //Esta función nos sirve para poder automatizar la animación de los botones del menú de navegación
    private void animateButtonPulse(int menuItemId) {
        //Localizamos el botón que queremos animar
        BottomNavigationView bottomNavigationView = findViewById(R.id.navView);
        View itemView = bottomNavigationView.findViewById(menuItemId);
        //Nos aseguramos de que el botón exista y lo animamos para que simule un parpadeo cambiando la escala
        if (itemView != null) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(itemView, "scaleX", 1f, 1.2f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(itemView, "scaleY", 1f, 1.2f, 1f);
            scaleX.setDuration(700);
            scaleY.setDuration(700);
            scaleX.setRepeatMode(ValueAnimator.REVERSE);
            scaleY.setRepeatMode(ValueAnimator.REVERSE);
            scaleY.setRepeatCount(5);
            scaleX.setRepeatCount(5);
            scaleX.start();
            scaleY.start();
        }
    }

    //Con esta función se inicializan las animaciones de la guía
    private void initializeAnimations() {
        AnimationDrawable animationDrawableSpyro =
                (AnimationDrawable) guideBinding.animatedImageViewSpyro.getDrawable();
        AnimationDrawable animationDrawable =
                (AnimationDrawable) guideBinding.animatedImageView.getDrawable();
        AnimationDrawable animationDrawable2 =
                (AnimationDrawable) guideBinding.animatedImageView2.getDrawable();
        AnimationDrawable animationDrawable3 =
                (AnimationDrawable) guideBinding.animatedImageView3.getDrawable();

        //Iniciamos las animaciones para poder ver a Spyro volando y fuego parte inferior de la pantalla
        animationDrawableSpyro.start();
        animationDrawable.start();
        animationDrawable2.start();
        animationDrawable3.start();
    }

    private boolean selectedBottomMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.nav_characters)
            navController.navigate(R.id.navigation_characters);
        else if (menuItem.getItemId() == R.id.nav_worlds)
            navController.navigate(R.id.navigation_worlds);
        else
            navController.navigate(R.id.navigation_collectibles);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Infla el menú
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Gestiona el clic en el ítem de información
        if (item.getItemId() == R.id.action_info) {
            showInfoDialog();  // Muestra el diálogo
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showInfoDialog() {
        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_about)
                .setMessage(R.string.text_about)
                .setPositiveButton(R.string.accept, null)
                .show();
    }

    //Con esta función iniciamos el video para el EasterEgg de la app que muestra un video al clicar 4 veces en la gema
    public void initializeVideo() {
        //Se muestra un Toast para informar al usuario de que se ha activado el EasterEgg
        Toast.makeText(this, "EasterEgg video activado", Toast.LENGTH_SHORT).show();
        //Se hace visible la pantalla que contiene el video
        videoBinding.videoLayout.setVisibility(View.VISIBLE);
        //Se carga el video
        videoBinding.video.setVideoPath("android.resource://" + this.getPackageName() + "/" + R.raw.video_spyro);
        videoBinding.video.start();
        //Si se pulsa la pantalla se para el video
        videoBinding.videoLayout.setOnClickListener(this::stopVideo);
        //Si se acaba el video se redirige al usuario a la pantalla de collectibles
        videoBinding.video.setOnCompletionListener(this::finishVideo);
    }

    //Al iniciar este metodo paramos el video para el EasterEgg
    private void stopVideo(View view) {
        videoBinding.videoLayout.setVisibility(View.GONE);
        videoBinding.video.stopPlayback();
    }

    //Cuando el video acaba se redirige al usuario a la pantalla de collectibles y se liberan recursos
    private void finishVideo(MediaPlayer mediaPlayer) {
        mediaPlayer.release();
        videoBinding.videoLayout.setVisibility(View.GONE);
        navController.navigate(R.id.navigation_collectibles);
    }

    //Con esta función se activa la animación del fuego para el EasterEgg
    public void initializeFlamesAnimation() {
        Toast.makeText(this, "EasterEgg animación de fuego activado", Toast.LENGTH_SHORT).show();
        //Se añade un sonido de fuego al activarse la animacion
        mediaPlayer = MediaPlayer.create(this, R.raw.fire_sound_effect);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        //Se lanza el canvas con la animación del fuego
        FlamesCanvasView flamesCanvasView = (this.findViewById(R.id.flameView));
        flamesCanvasView.animationLocalitation(400, 450, 30);
        flamesCanvasView.setVisibility(View.VISIBLE);
        //Cuando se pulsa la pantalla se para la animación
        flamesCanvasView.setOnClickListener(this::stopFlamesAnimation);
    }

    //Con esta funcion paramos la animación del fuego para el EasterEgg
    private void stopFlamesAnimation(View view) {
        //Paramos el sonido
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        //Eliminamos los circulos que se han creado para simular la animación de fuego y se oculta la pantalla
        FlamesCanvasView flamesCanvasView = (this.findViewById(R.id.flameView));
        flamesCanvasView.removeCircles();
        flamesCanvasView.setVisibility(View.GONE);
    }
}