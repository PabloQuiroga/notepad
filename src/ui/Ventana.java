package ui;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Interfaz de Bloc de notas basico
 * 
 * @version 1.0
 * fecha 28/8/2016
 * 
 * @author Pablo Daniel Quiroga
 */
public class Ventana {
	
    private JFrame window;
    private JMenuBar barra;
    private JMenu menuFile, menuEdit, menuHelp;
    private JMenuItem itNuevo, itAbrir, itSave, itSaveAs, itClose;
    private JMenuItem itFormat;
    private JMenuItem itAbout;
    private JScrollPane panel;
    private JTextArea area;
	
    private final String titulo = "Editor de textos";
    private String nameFile = "";
    File archivo = null;
	
    public Ventana(){
	initComponents();
	window.setTitle(titulo);
	window.setSize(800, 600);
	window.setVisible(true);
	window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
    /**
     * Metodo para inicializar componentes
     */
    @SuppressWarnings("Convert2Lambda")
    private void initComponents(){
    	//inicializa los elementos
        window = new JFrame();
        
        barra = new JMenuBar();
        
        menuFile = new JMenu("Archivo");
        menuEdit = new JMenu("Edicion");
        menuHelp = new JMenu("Ayuda");
        
        //items del menu Archivo
        itNuevo = new JMenuItem("Nuevo");
        itAbrir = new JMenuItem("Abrir");
        itSave = new JMenuItem("Guardar");
        itSaveAs = new JMenuItem("Guardar como");
        itClose = new JMenuItem("Salir");
        //items del menu Edicion
        itFormat = new JMenuItem("Ajuste de Linea");
        //items del menu Ayuda
        itAbout = new JMenuItem("Acerca de");
        
        //añade elementos al menu
        menuFile.add(itNuevo);
        menuFile.add(itAbrir);
        menuFile.add(itSave);
        menuFile.add(itSaveAs);
        menuFile.add(itClose);
        
        menuEdit.add(itFormat);
        
        menuHelp.add(itAbout);
        
        //añade menus a la barra
        barra.add(menuFile);
        barra.add(menuEdit);
        barra.add(menuHelp);
        //añade la barra a la ventana
        window.setJMenuBar(barra);
        
        // Crea un area de texto con scroll y lo añade a la ventana
        area = new JTextArea();
        panel = new JScrollPane(area);
        window.add(panel);
        
        
        // Asigna a cada menuItem su listener
        itNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevo();
            }
        });
        itAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrir();
            }
        });
        itSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        itSaveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarComo();
            }
        });
        itClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(descartar()){
                    window.dispose();
                }
            }
        });
        itFormat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFormat();
            }
        });
        itAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
    }

    /**
     * metodos para el manejo de eventos
     */
    
    /**
     * Comprueba contenido y ofrece descartar
     * @return boolean
     */
    private boolean descartar(){
        boolean opcion;
        boolean contiene = !area.getText().isEmpty();
        if(contiene){
            int x = JOptionPane.showConfirmDialog(window, "Desea descartar el contenido existente?");
            if(x == JOptionPane.YES_OPTION){
                opcion = true;
            }else{
                opcion = false;
            }
        }else{
            opcion = true;
        }
        return opcion;
    }
    
    /**
     * Comprueba y limpia entorno
     */
    private void nuevo(){
        if(descartar()){
            area.setText("");
            nameFile = "Nuevo";
            window.setTitle(titulo + " - " + nameFile);
        }
    }
    /**
     * Ofrece busqueda y abre archivo seleccionado
     */
    private void abrir(){
        if(descartar()){
		
            JFileChooser selector = new JFileChooser();
            FileNameExtensionFilter filtro = new FileNameExtensionFilter("txt files", "txt");
            selector.setFileFilter(filtro);
		
            int returnVal = selector.showOpenDialog(null);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                archivo = selector.getSelectedFile();
                try{
                    StringBuilder contenido = new StringBuilder();
                    BufferedReader out = new BufferedReader(new FileReader(archivo));
                    String parrafo;
                
                    while((parrafo = out.readLine()) != null){
                        contenido.append(parrafo).append("\n");
                    }
                    out.close();
                    area.setText(contenido.toString());
                    nameFile = archivo.getName();
                    window.setTitle(titulo + " - " + nameFile);
                
                }catch(FileNotFoundException ex){
                    JOptionPane.showMessageDialog(window, 
                        ex.getMessage(), 
                        "Error al abrir el archivo", 
                        JOptionPane.ERROR_MESSAGE);
                }catch(IOException ex){
                    JOptionPane.showMessageDialog(window, 
                        ex.getMessage(), 
                        "Error al leer", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    /**
     * Sobreescribe archivo preexisteente
     */
    private void guardar(){
        try{
            Scanner texto = new Scanner(area.getText());
            BufferedWriter out = new BufferedWriter(new FileWriter(archivo));
            String parrafo = "";
            parrafo = texto.nextLine();
            out.write(parrafo, 0, parrafo.length());
            while(texto.hasNextLine()){
                parrafo = texto.nextLine();
                out.newLine();
                out.append(parrafo);
            }
            texto.close();
            out.close();
            JOptionPane.showMessageDialog(window, 
                    "Archivo guardado con exito");
        }catch(IOException ex){
            JOptionPane.showMessageDialog(window, 
                    ex.getMessage(), 
                    "Error al guardar", 
                    JOptionPane.ERROR_MESSAGE);
        }catch(NullPointerException ex){
            guardarComo();
        }
    }
    /**
     * Crea archivo nuevo y guarda contenido
     */
    private void guardarComo(){
        try{
            JFileChooser selector = new JFileChooser();
            selector.showSaveDialog(window);
            archivo = new File(selector.getSelectedFile() + ".txt");
            
            Scanner texto = new Scanner(area.getText());
            BufferedWriter out = new BufferedWriter(new FileWriter(archivo));
            String parrafo = "";
            parrafo = texto.nextLine();
            out.write(parrafo, 0, parrafo.length());
            while(texto.hasNextLine()){
                parrafo = texto.nextLine();
                out.newLine();
                out.append(parrafo);
            }
            texto.close();
            out.close();
            JOptionPane.showMessageDialog(window, 
                    "Archivo guardado con exito");
        }catch(IOException ex){
            JOptionPane.showMessageDialog(window, 
                    ex.getMessage(), 
                    "Error al guardar", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Provee ajuste de linea al entorno
     */
    private void setFormat(){
        area.setLineWrap(true);
    }
    /**
     * Muestra informacion acerca del soft
     */
    private void about(){
        String soft = "Bloc de Notas";
        String descripcion = "Realizado en Java por\nPablo Daniel Quiroga\n"
                + "revision 1.0";
        JOptionPane.showMessageDialog(window, descripcion, soft, JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * pruebas
     * @param args
     */
    @SuppressWarnings("unused")
	public static void main(String[] args) {
        Ventana app = new Ventana();
        
    }
}