package evaluation.evalBench.panel;

import evaluation.evalBench.EvaluationResources;
import evaluation.evalBench.task.QuantitativeQuestion;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;

/**
 * Subclass of {@link QuantitativeQuestionPanelStrategy} Task Panel Strategy for
 * a {@link QuantitativeQuestion} with a JTextField for the answer
 * 
 * @author Stephan Hoffmann
 */
public class QuantitativeQuestionPanelStrategy extends QuestionPanelStrategy {
	//private JTextField textField;

	private JComponent inputComponent;

	private JLabel currentLabel;
	private double sliderFactor = 1;


	/**
	 * constructor taking a {@link QuantitativeQuestion}
	 * 
	 * @param aQuestion
	 *            {@link QuantitativeQuestion}
	 */
	public QuantitativeQuestionPanelStrategy(QuantitativeQuestion aQuestion) {
		super(aQuestion);

	}

	/**
	 * checks for correct input for this task and sets the answer value
	 * 
	 * @return false if empty false if {@link QuantitativeQuestion} is integer
	 *         and input was not an integer false if
	 *         {@link QuantitativeQuestion} is not integer and the answer does
	 *         not contain a double/float value
	 */
	@Override
	public boolean checkForCorrectInput() {

		QuantitativeQuestion quantTask = (QuantitativeQuestion) this.getQuestion();

		if (quantTask.isInteger()) {
			try {
				if (quantTask.getUseSpinner())
					quantTask.setGivenAnswer(Double
							.parseDouble(((JSpinner) inputComponent).getValue()
									.toString()));
				else
					quantTask.setGivenAnswer((double) ((JSlider) inputComponent)
							.getValue()/sliderFactor);

				this.setErrorMessage("");
				return true;

			} catch (Exception e) {
				this.setErrorMessage(EvaluationResources.getString("quantitativequestion.errorInt"));
				return false;
			}
		} else {
			try {
				if (quantTask.getUseSpinner())
					quantTask.setGivenAnswer(Double
							.parseDouble(((JSpinner) inputComponent).getValue()
									.toString()));
				else {
					quantTask.setGivenAnswer(Double.parseDouble(String
							.valueOf(((JSlider) inputComponent).getValue()/sliderFactor)));
				}

				this.setErrorMessage("");
				return true;

			} catch (Exception e) {
				this.setErrorMessage(EvaluationResources.getString("quantitativequestion.errorDouble"));
			}
		}

		return false;

		// if (!(textField.getText().length() > 0)) {
		//
		// this.errorMessage = (EvaluationResources
		// .getString("quanttaskpanel.errorNumber"));
		// return false;
		// } else {
		// QuantitativeQuestion quantTask = (QuantitativeQuestion)
		// this.getTask();
		//
		// if (quantTask.isInteger()) {
		// try {
		// Integer.parseInt(textField.getText());
		//
		// } catch (NumberFormatException ex) {
		// this.errorMessage = (EvaluationResources
		// .getString("quanttaskpanel.errorInt"));
		// return false;
		// }
		// } else {
		// try {
		// Double.parseDouble(textField.getText());
		// //
		// quantTask.setAnsweredValue(Double.parseDouble(textField.getText()));
		// } catch (NumberFormatException ex) {
		// this.errorMessage = (EvaluationResources
		// .getString("quanttaskpanel.errorNumber"));
		// return false;
		// }
		// }
		//
		// }
		//
		// return true;
	}

	private void updateSliderLabel() {
		if (sliderFactor == 1.0) {
			currentLabel.setText(Integer.toString(((JSlider) inputComponent).getValue()));
		} else {
			currentLabel.setText(Double.toString(((JSlider) inputComponent).getValue() / sliderFactor));
		}
	}

	/**
	 * provides an answering field (JPanel) with a JTextField
	 * 
	 * @return an answering field
	 */
	@Override
	public JPanel getNewAnsweringPanel() {

		QuantitativeQuestion quantitativeTask = (QuantitativeQuestion) super
				.getQuestion();

		if (! quantitativeTask.getUseSpinner()) {

			if (quantitativeTask.isInteger()) {
				sliderFactor = 1;
			} else {
				sliderFactor = 10;
			}
			int min = (int) (quantitativeTask.getMinimum() * sliderFactor);
			int max = (int) (quantitativeTask.getMaximum() * sliderFactor);
			int value = (min + max) / 2;
			
			inputComponent = new JSlider(min, max, value);

//			((JSlider) inputComponent).setMajorTickSpacing(20);;
//			((JSlider) inputComponent).setPaintLabels(true);
//			((JSlider) inputComponent).setPaintLabels(true);

			String minStr = quantitativeTask.getMinimumString();
			if (minStr == null) {
				if (quantitativeTask.isInteger()) {
					minStr = Integer.toString((int) quantitativeTask.getMinimum());
				} else {
					minStr = Double.toString(quantitativeTask.getMinimum());
				}
			}
			JLabel minLabel = new JLabel(minStr);

			String maxStr = quantitativeTask.getMaximumString();
			if (maxStr == null) {
				if (quantitativeTask.isInteger()) {
					maxStr = Integer.toString((int) quantitativeTask.getMaximum());
				} else {
					maxStr = Double.toString(quantitativeTask.getMaximum());
				}
			}
			JLabel maxLabel = new JLabel(maxStr, SwingConstants.RIGHT);

			this.currentLabel = new JLabel("", SwingConstants.CENTER);
			updateSliderLabel();
			((JSlider) inputComponent).addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					updateSliderLabel();
				}
			});
			
//			JPanel innerPanel = new JPanel();
//			innerPanel.setLayout(new BorderLayout());
//			innerPanel.add(inputComponent, BorderLayout.CENTER);
			
			JPanel labels = new JPanel(new GridLayout(1, 3));
			labels.add(minLabel);
			labels.add(currentLabel);
			labels.add(maxLabel);

			answeringPanel.add(labels, BorderLayout.SOUTH);
			answeringPanel.add(inputComponent, BorderLayout.CENTER);

		} else {
			inputComponent = new JSpinner();
			((JSpinner) inputComponent).setModel(new SpinnerNumberModel(
					(quantitativeTask.getMaximum() + quantitativeTask
							.getMinimum()) / 2, quantitativeTask.getMinimum(),
					quantitativeTask.getMaximum(), quantitativeTask
							.getStepsize()));
//			((JSpinner) inputComponent).setEditor(new JFormattedTextField(java.text.NumberFormat.getNumberInstance()));

			answeringPanel.add(inputComponent, BorderLayout.CENTER);
		}

		// textField = new JTextField(5);
		//
		// textField.setMinimumSize(new Dimension(50, 20));
		// textField.setPreferredSize(new Dimension(50, 20));
		// textField.setMaximumSize(new Dimension(100,
		// Short.MAX_VALUE));
		//
		// JPanel answeringPanel = new JPanel();
		//
		// answeringPanel.setLayout(new BoxLayout(answeringPanel,
		// BoxLayout.X_AXIS));
		//
		// {
		// JLabel jLabel1 = new JLabel();
		//
		// QuantitativeQuestion quantTask =
		// (QuantitativeQuestion)this.getTask();
		// jLabel1.setText(quantTask.getUnit());
		//
		// answeringPanel.add(Box.createRigidArea(new Dimension(5, 5)));
		// answeringPanel.add(textField);
		// answeringPanel.add(Box.createRigidArea(new Dimension(15, 5)));
		// answeringPanel.add(jLabel1);
		// answeringPanel.add(Box.createHorizontalGlue());
		//
		//
		//
		// jLabel1.setBackground(Color.WHITE);
		// }

		return answeringPanel;
	}

	@Override
	public void inputFinished() {

		// if (quantTask.isInteger()){
		// try{
		// quantTask.setAnsweredValue(Integer.parseInt(textField.getText()));
		// }catch (NumberFormatException ignored){
		//
		// }
		// }else {
		// try{
		//
		// quantTask.setAnsweredValue(Double.parseDouble(textField.getText()));
		// }catch (NumberFormatException ignored){
		//
		// }
		// }
	}
}
