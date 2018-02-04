package org.waltonrobotics;

import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Controller implements Initializable {

	private final SimpleDoubleProperty seconds = new SimpleDoubleProperty(0.0);
	private final long timeIncrement = 100;
	public VBox blueSwitch;
	public VBox scale;
	public VBox redSwitch;
	public ToggleButton blueForce;
	public ToggleButton blueBoost;
	public ToggleButton blueLevitate;
	public Label bluePoints;
	public Label redPoints;
	public ToggleButton gameToggleButton;
	public ToggleButton redLevitate;
	public ToggleButton redBoost;
	public ToggleButton redForce;
	public FieldElement[] fieldElements = new FieldElement[3];
	public Button redSideSwitch2;
	public Button redSideSwitch1;
	public Button leverButton1;
	public Button neutralLever;
	public Button leverButton2;
	public Button blueSideSwitch2;
	public Button blueNeutralSwitch;
	public Button blueSideSwitch1;
	public Button redNeutralSwitch;
	public Label gameTime;
	public Button resetButton;
	private SimpleIntegerProperty blueScore = new SimpleIntegerProperty(0);
	private SimpleIntegerProperty redScore = new SimpleIntegerProperty(0);

	public void randomizeElents() {
		for (FieldElement fieldElement : fieldElements) {
			fieldElement.randomizeSides();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bluePoints.textProperty().bind(Bindings.concat("Blue: ", blueScore));
		redPoints.textProperty().bind(Bindings.concat("Red: ", redScore));

		FieldElement.setScore(2);

		Timeline timeline = new Timeline(new KeyFrame(
			Duration.millis(timeIncrement),
			event -> {
				seconds.set(seconds.doubleValue() + timeIncrement / 1000.0);
				if (Math.round(seconds.get()) == 15.0) {
					Toolkit.getDefaultToolkit().beep();
					FieldElement.setScore(1);
				}

			}));
		timeline.setCycleCount(Animation.INDEFINITE);

		fieldElements[0] = new FieldElement(blueSideSwitch1, blueNeutralSwitch, blueSideSwitch2,
			Color.BLUE, blueScore);
		fieldElements[1] = new FieldElement(leverButton1, neutralLever, leverButton2, Color.BLUE,
			blueScore, redScore);
		fieldElements[2] = new FieldElement(redSideSwitch1, redNeutralSwitch, redSideSwitch2,
			Color.RED, redScore);

		gameTime.textProperty().bind(Bindings.concat("Time: ", seconds.asString("%3.1f")));

		for (FieldElement fieldElement : fieldElements) {
			fieldElement.stop();
		}

		gameToggleButton.setOnAction(event ->
			{
				if (gameToggleButton.isSelected()) {
					gameToggleButton.setText("Stop");
					timeline.play();
					resetButton.setDisable(true);

					for (FieldElement fieldElement : fieldElements) {
						fieldElement.play();
					}
				} else {
					gameToggleButton.setText("Start");
					resetButton.setDisable(false);
					timeline.stop();

					for (FieldElement fieldElement : fieldElements) {
						fieldElement.stop();
					}
				}
			}
		);

		resetButton.setOnAction(event -> {
			randomizeElents();
			seconds.set(0);
			blueScore.set(0);
			redScore.set(0);

			for (FieldElement fieldElement : fieldElements) {
				fieldElement.reset();
			}
		});

		randomizeElents();
	}
}