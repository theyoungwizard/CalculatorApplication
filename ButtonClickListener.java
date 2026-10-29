/* ButtonClickListener.java
Class that implements the ActionListener to handle button clicks in the calculator

display - a WatermarkTextField with a background image containing current input or result of a calculation
current - a double representing the result of the previous calculation
operator - a char representing the current operator (+, -, *, /)
currentValue - a String representing the current value in the display

*/

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ButtonClickListener implements ActionListener{
    private final WatermarkTextField display;
    private double current;
    private char operator;
    private String currentValue;
    private double secondOperand;

    public ButtonClickListener(WatermarkTextField display){
        this.display = display;
    }

/* 
 * Checks if the display is showing "Undefined" and clears the display if it is      
 */
    public void isUndefined() {
        if (display.getText().equals("Undefined")) {
            display.setText("");
        }
    }

/* 
 * If the input is a whole number, display it without a decimal point
 *
 * @param value   The double being changed
 * 
 */
    public void doubleToInt(double value) {
        if (value % 1 == 0) {
            display.setText(String.valueOf((int) value));
        }
        else {
            display.setText(String.valueOf(value));
        }
    }

/* 
 * Checks the operator and returns a calculated value based on the operator
 *
 * @return value   The result of the current value and a second operand in a math operation
 * 
 */
    public double operatorCheck() {
        double value = 0;
        switch(operator) {
                case '+':
                    value = current + secondOperand;
                    break;
                case '-':
                    value = current - secondOperand;
                    break;
                case '*':
                    value = current * secondOperand;
                    break;
                case '/':
                    if (secondOperand == 0) {
                        display.setText("Undefined");
                        //return;
                    }
                    value = current / secondOperand;
                    break;
            }
            return value;  
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        
        if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.equals(".")){
            String text = display.getText();
            //Ensures user cannot add multiple decimal points to a number
            if (command.equals(".") && text.contains(".")) {
                return; 
            }
            isUndefined();
            display.setText(text  + command);
        }

        else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
            String text = display.getText();
            //Prevents operator button from being pressed before a number
            if (text.isEmpty()) {
                return; 
            }
            isUndefined();
            // result = Double.parseDouble(display.getText());
            current = Double.parseDouble(text);
            operator = command.charAt(0);
            display.setText("");
        }

        //Displays current value as a percentage of 100
        else if (command.equals("%")) {
            isUndefined();
            current = Double.parseDouble(display.getText());
            current = current / 100;
            doubleToInt(current);
        }


        //Clears display and resets variables
        else if (command.equals("AC")) {
            display.setText("");
            current = 0;
            operator = '\0';
        }

        /*Empty or 0 = display "-", 
        Positive number = display negative number,
        Negative number = display positive number*/
        else if (command.equals("-/+")) { //empty string exception when typing - with no digit
            isUndefined();

            currentValue = display.getText();
            
            if (currentValue.isEmpty() || currentValue.equals("0")) {
                display.setText("-");
                currentValue = display.getText();
                if(currentValue.equals("-")){
                    return;
                }

            }

            currentValue = display.getText();

            if(currentValue.equals("-")){
                return;
            }

            double val = Double.parseDouble(currentValue);

            if (val > 0) {
                val = -val;
                doubleToInt(val);
            }

            else if (val < 0) {
                val = val + (-2 * val);
                doubleToInt(val);
            }
        }

        //When the display is not empty, deletes last digit/symbol
        else if (command.equals("CE")) {
            isUndefined();
            currentValue = display.getText();
            if (!currentValue.isEmpty()) {
                display.setText(currentValue.substring(0, currentValue.length() - 1)); //deletes one digit from the display
            }
        }

        //Calculates and displays result
        else if (command.equals("=")) {

            isUndefined();

            //Prevents the equals sign from being pressed before a number
            if (display.getText().isEmpty()) {
                return; 
            }

            //Prevents change to text field if no operator is pressed
            if (operator == '\0') {
                return; 
            }

            secondOperand = Double.parseDouble(display.getText());

            double answer = operatorCheck();
            if (operator == '/') {
                if (secondOperand == 0) {
                        display.setText("Undefined");
                        return;
                    }
            } 
            display.setText("");

            doubleToInt(answer);
        }
    }
}
