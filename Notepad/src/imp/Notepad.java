package imp;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

/**
 * Interfaz de Bloc de notas basico
 * 
 * @version 1.1
 * @since fecha 10/7/2017
 * 
 * @author Pablo Daniel Quiroga
 */
public class Notepad {

	private JFrame window;
	private JMenuBar barra;
	private JMenu menuFile, menuEdit, menuView, menuHelp;
	private JMenuItem itNuevo, itAbrir, itSave, itSaveAs, itClose;
	private JMenuItem itFormat;
	private JMenuItem itFondo, itFuente;
	private JMenuItem itAbout;
	private JScrollPane panel;
	private static JTextArea area;
	
	private final String titulo = "Editor de textos";
	private String nameFile = "";
	File archivo = null;
	
	private Font fuente;
	
	public Notepad() {
		fuente = new Font("Arial", Font.PLAIN, 11);
		initComponents();
		window.setTitle(titulo);
		window.setSize(800, 600);
		window.setResizable(false);
		window.setVisible(true);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	}

	/**
	 * Metodo para inicializar componentes
	 */
	private void initComponents() {

		// inicializa los elementos
		window = new JFrame();

		barra = new JMenuBar();

		menuFile = new JMenu("Archivo");
		menuEdit = new JMenu("Edicion");
		menuHelp = new JMenu("Ayuda");

		// items del menu Archivo
		itNuevo = new JMenuItem("Nuevo");
		itAbrir = new JMenuItem("Abrir");
		itSave = new JMenuItem("Guardar");
		itSaveAs = new JMenuItem("Guardar como");
		itClose = new JMenuItem("Salir");
		// items del menu Edicion
		itFormat = new JMenuItem("Ajuste de Linea");
		// items del menu Ayuda
		itAbout = new JMenuItem("Acerca de");

		// elementos de menu
		menuFile.add(itNuevo);
		menuFile.add(itAbrir);
		menuFile.add(itSave);
		menuFile.add(itSaveAs);
		menuFile.add(itClose);

		menuEdit.add(itFormat);

		menuHelp.add(itAbout);

		// añade menus en la barra
		barra.add(menuFile);
		barra.add(menuEdit);		
		
		menuView = new JMenu("Ver");
		barra.add(menuView);
		itFondo = new JMenuItem("Fondo");
		menuView.add(itFondo);
		itFuente = new JMenuItem("Fuentes");
		menuView.add(itFuente);
		
		barra.add(menuHelp);
		// añade la barra a la ventana
		window.setJMenuBar(barra);

		// Crea un area de texto con scroll y lo añade a la ventana
		area = new JTextArea();
		area.setFont(fuente);
		panel = new JScrollPane(area);
		window.getContentPane().add(panel);

		// Gestion de eventos de ventana y menu
		ManejoDeEventos eventos = new ManejoDeEventos();

		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (eventos.descartar()) {
					window.dispose();
				}
			}
		});

		itNuevo.addActionListener(eventos);
		itAbrir.addActionListener(eventos);
		itSave.addActionListener(eventos);
		itSaveAs.addActionListener(eventos);
		itClose.addActionListener(eventos);
		itFormat.addActionListener(eventos);
		itFuente.addActionListener(eventos);
		itFondo.addActionListener(eventos);
		itAbout.addActionListener(eventos);

	}

	/**
	 * Clase interna para el manejo de eventos de menu
	 */
	private class ManejoDeEventos implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent evt) {
			// Comprueba y limpia entorno
			if (evt.getSource() == itNuevo) {
				if (descartar()) {
					area.setText("");
					nameFile = "Nuevo";
					window.setTitle(titulo + " - " + nameFile);
				}
			}
			// Ofrece busqueda y abre archivo seleccionado
			else if (evt.getSource() == itAbrir) {
				if (descartar()) {
					archivo = Selector.buscar();
					if (archivo != null) {
						try {
							StringBuilder contenido = new StringBuilder();
							BufferedReader out = new BufferedReader(new FileReader(archivo));
							String parrafo;

							while ((parrafo = out.readLine()) != null) {
								contenido.append(parrafo).append("\n");
							}
							out.close();
							area.setText(contenido.toString());
							nameFile = archivo.getName();
							window.setTitle(titulo + " - " + nameFile);

						} catch (FileNotFoundException ex) {
							JOptionPane.showMessageDialog(window, ex.getMessage(), "Error al abrir el archivo",
									JOptionPane.ERROR_MESSAGE);
						} catch (IOException ex) {
							JOptionPane.showMessageDialog(window, ex.getMessage(), "Error al leer",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
			// Sobreescribe archivo preexistente
			else if (evt.getSource() == itSave) {
				try {
					Scanner texto = new Scanner(area.getText());
					BufferedWriter out = new BufferedWriter(new FileWriter(archivo));
					String parrafo = "";
					parrafo = texto.nextLine();
					out.write(parrafo, 0, parrafo.length());
					while (texto.hasNextLine()) {
						parrafo = texto.nextLine();
						out.newLine();
						out.append(parrafo);
					}
					texto.close();
					out.close();
					JOptionPane.showMessageDialog(window, "Archivo guardado con exito");
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(window, ex.getMessage(), "Error al guardar",
							JOptionPane.ERROR_MESSAGE);
				}
			}
			// Crea nuevo archivo y guarda contenido
			else if (evt.getSource() == itSaveAs) {
				archivo = Selector.guardar();

				if (archivo != null) {
					try {
						Scanner texto = new Scanner(area.getText());
						BufferedWriter out = new BufferedWriter(new FileWriter(archivo));
						String parrafo = "";
						parrafo = texto.nextLine();
						out.write(parrafo, 0, parrafo.length());
						while (texto.hasNextLine()) {
							parrafo = texto.nextLine();
							out.newLine();
							out.append(parrafo);
						}
						texto.close();
						out.close();

					} catch (IOException ex) {
						JOptionPane.showMessageDialog(window, ex.getMessage(), "Error al guardar",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else if (evt.getSource() == itClose) {
				if (descartar()) {
					window.dispose();
				}
			}
			// Provee ajuste de linea al entorno
			else if (evt.getSource() == itFormat) {
				area.setLineWrap(true);
			}
			// Muestra informacion acerca del soft
			else if (evt.getSource() == itAbout) {
				String soft = "Bloc de Notas";
				String descripcion = "Realizado en Java por\nPablo Daniel Quiroga\n" + "revision 1.1";
				JOptionPane.showMessageDialog(window, descripcion, soft, JOptionPane.INFORMATION_MESSAGE);
			}
			//Provee selector de color para el fondo
			else if(evt.getSource() == itFondo){
				Color newcolor = JColorChooser.showDialog(
													window, 
													"Seleccione un nuevo fondo", 
													area.getBackground());
				if(newcolor != null){
					area.setBackground(newcolor);
				}
			}
			//Provee selector de fuente
			else if(evt.getSource() == itFuente){
				//TODO aun no me toma la nueva fuente
				FontChooser selector = new FontChooser();
				fuente = selector.getFuente();
				area.setFont(fuente);				
			}
		}

		/**
		 * Comprueba contenido y ofrece descartar
		 * @return boolean
		 */
		private boolean descartar() {
			boolean opcion;
			boolean contiene = !area.getText().isEmpty();
			if (contiene) {
				int x = JOptionPane.showConfirmDialog(window, "Desea descartar el contenido existente?");
				if (x == JOptionPane.YES_OPTION) {
					opcion = true;
				} else {
					opcion = false;
				}
			} else {
				opcion = true;
			}
			return opcion;
		}
	}

	//TEST
	public static void main(String[] args) {
		Notepad app = new Notepad();
	}
}
