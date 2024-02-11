package tool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

 
public class Calculator2 extends JFrame implements ActionListener {

	private JPanel panel, tPanel;
	private JTextField tField, rField;
	private JButton[] buttons;
	private String[] labels = { 
			"", "", "", "CE", "%",
			"7", "8", "9", "/", "sqrt",
			"4", "5", "6", "x", "x^2",
			"1", "2", "3", "-", "1/x",
			"0", "+/-", ".", "+", "=",
	};

	private Map<String, Integer> map = new HashMap<String, Integer>();
	private String[] ops = {
			"%", "/", "(sqrt)", "x", "(x^2)", "-", "(1/x)", "(+/-)", "+", "="	
	};
	private int[] priorities = {
			3, 3, 4, 3, 4, 2, 4, 4, 2, 1	
	};

	Stack<Double> numStack = new Stack<>();
	Stack<String> opStack= new Stack<>();
	
	StringBuffer num = new StringBuffer("");
	
	private String prev_operation = "";
	
	double res;
//vildlvvd deer recognize hiih bas herhen ene vildel hiigdej baigaad shalgah 
	public void processOp(int priority) {
	    double d1, d2;
	    int prev_prio;
	    while (!opStack.isEmpty()) {
	        String s = opStack.pop();
	        if (s.equals("(")) {
	            opStack.push(s);
	            break;
	        }
	        prev_prio = map.get(s);
	        if (s.startsWith("(")) {
	            s = s.substring(1, s.length() - 1);
	            d2 = numStack.pop();
	            switch (s) {
	                case "sqrt":
	                    res = Math.sqrt(d2);
	                    break;
	                case "x^2":
	                    res = d2 * d2;
	                    break;
	                case "1/x":
	                    if (d2 != 0) {
	                        res = 1 / d2;
	                    } else {
	                        rField.setText("Error: Division by zero");
	                        return;
	                    }
	                    break;
	                case "+/-":
	                    res = -d2;
	                    break;
	            }
	            numStack.push(res);
	        } else if (prev_prio >= priority) {
	            d2 = numStack.pop();
	            if (!opStack.isEmpty() && opStack.peek().equals("(")) {
	                opStack.pop();
	                d1 = numStack.pop();
	                switch (s) {
	                    case "sqrt":
	                        res = Math.sqrt(d1);
	                        break;
	                    case "x^2":
	                        res = d1 * d1;
	                        break;
	                    case "1/x":
	                        if (d1 != 0) {
	                            res = 1 / d1;
	                        } else {
	                            rField.setText("Error: Division by zero");
	                            return;
	                        }
	                        break;
	                    case "+/-":
	                        res = -d1;
	                        break;
	                }
	            } else {
	                d1 = numStack.pop();
	                switch (s) {
	                    case "+":
	                        res = d1 + d2;
	                        break;
	                    case "-":
	                        res = d1 - d2;
	                        break;
	                    case "x":
	                        res = d1 * d2;
	                        break;
	                    case "/":
	                        if (d2 != 0) {
	                            res = d1 / d2;
	                        } else {
	                            rField.setText("Error: Division by zero");
	                            return;
	                        }
	                        break;
	                    case "%":
	                        res = d1 % d2;
	                        break;
	                }
	            }
	            numStack.push(res);
	        } else {
	            opStack.push(s);
	            break;
	        }
	    }
	}



	public void processKey(String key) {
	    switch (key) {
	        case "CE":
	            numStack.clear();
	            opStack.clear();
	            num.setLength(0);
	            prev_operation = "";
	            tField.setText("");
	            rField.setText("");
	            break;
	        case "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".":
	            tField.setText(tField.getText() + key);
	            num.append(key);
	            break;
	        case "+", "-", "x", "/":
	            tField.setText(tField.getText() + key);
	            if (!num.toString().isEmpty()) {
	                numStack.push(Double.parseDouble(num.toString()));
	                processOp(map.get(key));
	                num.setLength(0);
	            }
	            opStack.push(key);
	            break;
	        case "sqrt", "x^2", "1/x", "+/-":
	            tField.setText(tField.getText() + "(" + key + ")");
	            if (!num.toString().isEmpty()) {
	                numStack.push(Double.parseDouble(num.toString()));
	                processOp(map.get(key));
	                num.setLength(0);
	            }
	            opStack.push("(" + key + ")");
	            break;
	        case "(":
	            tField.setText(tField.getText() + key);
	            opStack.push(key);
	            break;
	        case ")":
	            tField.setText(tField.getText() + key);
	            processOp(0);
	            break;
	        case "=":
	            if (!num.toString().isEmpty()) {
	                numStack.push(Double.parseDouble(num.toString()));
	                processOp(0); 
	                num.setLength(0);
	                if (numStack.size() != 1 || opStack.size() != 0)
	                    rField.setText("Error!!!");
	                else
	                    rField.setText(Double.toString(numStack.peek()));
	            }
	            break;
	        default:
	            break;
	    }
	}



	public void actionPerformed(ActionEvent e) {
    	JButton but = (JButton) e.getSource();
        String key = (String) but.getText();
        processKey(key);
    }

	public Calculator2() {
		for(int i=0; i<ops.length; i++)
			map.put(ops[i], priorities[i]);
		
		tField = new JTextField(35);
		rField = new JTextField(35);
		tPanel = new JPanel();
		panel = new JPanel();
		tField.setText("");
		tField.setEnabled(false);
		rField.setText("");
		rField.setEnabled(false);

		tPanel.setLayout(new GridLayout(2, 1, 3, 3));
		panel.setLayout(new GridLayout(0, 5, 3, 3));
		buttons = new JButton[25];
		int index = 0;
		for (int rows = 0; rows < 5; rows++) {
			for (int cols = 0; cols < 5; cols++) {
				buttons[index] = new JButton(labels[index]);
				if( cols >= 3 )
					buttons[index].setForeground(Color.red);
				else 
					buttons[index].setForeground(Color.blue);
				buttons[index].setBackground(Color.yellow);
				buttons[index].setOpaque(true);
				buttons[index].setBorderPainted(false);
				buttons[index].addActionListener(this);
				panel.add(buttons[index]);
				index++;
			}
		}
		
		tPanel.add(tField, BorderLayout.NORTH);
		tPanel.add(rField, BorderLayout.CENTER);
		add(tPanel, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
	}

    public static void main(String[] args) {
        new Calculator2();
    }
}
