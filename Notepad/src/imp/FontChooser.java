package imp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FontChooser extends JFrame {

	private JLabel sampleText = new JLabel("Label");
	private static JComboBox<String> fontComboBox;
	private static JComboBox<Integer> sizeComboBox;
	private static JCheckBox boldCheck, italCheck;
	private JButton btnAceptar;
	private String[] fonts;
	
	private static Font f;
	public static boolean bandera = false;

	public FontChooser() {
		f = new Font("Arial", Font.PLAIN, 12);
		
		setTitle("Selector de fuente");
		setSize(550, 150);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panelTexto = new JPanel();
		panelTexto.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelTexto.add(sampleText);
		
		add(panelTexto, BorderLayout.NORTH);

		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		fonts = g.getAvailableFontFamilyNames();

		JPanel controlPanel = new JPanel();
		fontComboBox = new JComboBox<>(fonts);
		controlPanel.add(new JLabel("Family: "));
		controlPanel.add(fontComboBox);

		Integer[] sizes = { 7, 8, 9, 10, 11, 12, 14, 18, 20, 22, 24, 36 };
		sizeComboBox = new JComboBox<>(sizes);
		sizeComboBox.setSelectedIndex(5);
		controlPanel.add(new JLabel("Size: "));
		controlPanel.add(sizeComboBox);

		boldCheck = new JCheckBox("Bold");
		controlPanel.add(boldCheck);

		italCheck = new JCheckBox("Ital");
		controlPanel.add(italCheck);

		btnAceptar = new JButton("Aceptar");
		controlPanel.add(btnAceptar);

		add(controlPanel, BorderLayout.SOUTH);
		
		setVisible(true);
		setLocationRelativeTo(null);
		
		//Manejo de eventos
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		fontComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateText();
			}
		});
		sizeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateText();
			}
		});
		boldCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateText();
			}
		});
		italCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateText();				
			}
		});
		btnAceptar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cerrar();
			}
		});
		
	}
	//Setea fuente en bloc de notas y cierra
	public void cerrar(){
		Notepad.setFuente(sampleText.getFont());
		this.dispose();
		
	}
	//Actualiza la vista de ejemplo
	public void updateText() {
		setFuente();
		sampleText.setFont(f);
	}
	//Setea nueva fuente
	public void setFuente(){
		String name = (String) fontComboBox.getSelectedItem();
		Integer size = (Integer) sizeComboBox.getSelectedItem();
		int style;
		if (boldCheck.isSelected() && italCheck.isSelected()){
			style = Font.BOLD | Font.ITALIC;
		}
		else if (boldCheck.isSelected()){
			style = Font.BOLD;
		}
		else if (italCheck.isSelected()){
			style = Font.ITALIC;
		}
		else{
			style = Font.PLAIN;
		}

		f = new Font(name, style, size.intValue());
	}
	 
	
}
