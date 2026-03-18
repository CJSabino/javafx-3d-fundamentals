package com.example.teste3d;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class HelloApplication extends Application {

    private double angulo = 0;
    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) {

        Sphere planeta = new Sphere(180);
        PhongMaterial matPlaneta = new PhongMaterial();


        Image earthImage = new Image(getClass().getResourceAsStream("/earth_map.jpg"));
        matPlaneta.setDiffuseMap(earthImage);

//Brilho
        matPlaneta.setSpecularColor(Color.WHITE);

        planeta.setMaterial(matPlaneta);


        Box satelite = new Box(20, 20, 20);
        PhongMaterial matSatelite = new PhongMaterial();
        matSatelite.setDiffuseColor(Color.SILVER);
        matSatelite.setSpecularColor(Color.WHITE);
        satelite.setMaterial(matSatelite);


        Group root = new Group(planeta, satelite);
        Scene scene = new Scene(root, 1280, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.web("#050505"));


        Rotate xRotate = new Rotate(0, Rotate.X_AXIS);
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        root.getTransforms().addAll(xRotate, yRotate);


        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);


        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });
        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + (anchorX - event.getSceneX()));
        });

        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-200);
        camera.setTranslateY(-100);
        camera.setNearClip(0.1);
        camera.setFarClip(5000.0);
        scene.setCamera(camera);
        // Zoom
        scene.setOnScroll(event -> {
            double delta = event.getDeltaY();
            camera.setTranslateZ(camera.getTranslateZ() + delta);
        });

        // Orbita
        final double RAIO_ORBITA = 250.0;
        final double VELOCIDADE = 0.02;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                double x = Math.cos(angulo) * RAIO_ORBITA;
                double z = Math.sin(angulo) * RAIO_ORBITA;
                satelite.setTranslateX(x);
                satelite.setTranslateZ(z);
                satelite.setRotate(angulo * 50);
                angulo += VELOCIDADE;
            }
        };
        timer.start();

        stage.setTitle("Teste de Sistema Orbital 3D");
        stage.setScene(scene);
        PointLight luz = new PointLight(Color.WHITE);
        luz.setTranslateX(-1200);
        luz.setTranslateY(-600);
        luz.setTranslateZ(-1200);

        //root.getChildren().add(luz);  //Comentado para melhor visualização
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}