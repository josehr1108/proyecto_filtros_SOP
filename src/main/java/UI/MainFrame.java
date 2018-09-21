package UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import UI.ImageFrame;

public class MainFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    private final String[] FILTER_LIST = {
        "Sepia",
        "Grises"
    };
    private final String[] PROC_TYPES = {"Secuencial","Paralelo"};

    private File selectedFile;
    private JComboBox filterDropdown;
    private JComboBox processingTypeDropdown;

    public MainFrame() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Filtrado de imagenes");   
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel,BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        inputPanel.setSize(500,400);

        //Label de selección de imagen
        JLabel fileLabel = new JLabel("Seleccione el archivo de imagen");
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 30;
        c.ipadx = 30;
        inputPanel.add(fileLabel,c);
        
        //Botón para buscar archivos locales
        JFileChooser fc = new JFileChooser();
        final JButton fileButton = new JButton("Examinar");
        fileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == fileButton) {
                    int returnVal = fc.showOpenDialog(MainFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        MainFrame.this.selectedFile = fc.getSelectedFile();
                        //This is where a real application would open the file.
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            }
        });
        c.gridx = 1;
        c.ipady = 0;
        c.ipadx = 0;
        inputPanel.add(fileButton,c);  
        
        //Label de selección de filtro
        JLabel filterLabel = new JLabel("Seleccione el filtro a aplicar");
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 30;
        c.ipadx = 30;
        inputPanel.add(filterLabel,c);
        
        //Dropdown de opciones de filtros
        filterDropdown = new JComboBox(FILTER_LIST);
        c.gridx = 1;
        c.ipady = 0;
        c.ipadx = 0;
        inputPanel.add(filterDropdown,c);  

        //Label de selección de modalidad
        JLabel processingTypeLabel = new JLabel("Seleccione la modalidad de procesamiento");
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 30;
        c.ipadx = 30;
        inputPanel.add(processingTypeLabel,c);

        //Dropdown de secuencial o paralelo
        processingTypeDropdown = new JComboBox(PROC_TYPES);
        c.gridx = 1;
        c.ipady = 0;
        c.ipadx = 0;
        inputPanel.add(processingTypeDropdown,c);

        this.add(inputPanel,BorderLayout.CENTER);

        JButton beginButton = new JButton("Aplicar");
        beginButton.setAlignmentY(SwingConstants.CENTER);
        beginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.invokeImageView();
            }
        });
        add(beginButton,BorderLayout.SOUTH);

        this.setTitle("Main");
        this.setSize(800,600);
        this.setVisible(true);
    }

    private void invokeImageView(){
        String selectedFilter = String.valueOf(MainFrame.this.filterDropdown.getSelectedItem());
        String selectedOpMode = String.valueOf(MainFrame.this.processingTypeDropdown.getSelectedItem());
        
        ImageFrame imageView = new ImageFrame(this.selectedFile);
    }
}