package UI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import FilterClasses.BrigthScale;
import FilterClasses.GrayScale;
import FilterClasses.InvertScale;
import FilterClasses.SepiaScale;
import UI.ImageFrame;

public class MainFrame extends JFrame{

    private static final long serialVersionUID = 1L;
    private final String[] FILTER_LIST = {
        "Escala de Grises",
        "Sepia",
        "Opacidad",
        "Color Invertido",
        "Desenfoque Gaussiano",
        "Ajuste de brillo",
        "Compresión con pérdida",
        "Compresión sin pérdida",
        "Segmentación",
        "Textura",
    };
    private final String[] PROC_TYPES = {"Secuencial","Paralelo"};
    private JPanel inputPanel;
    GridBagConstraints panelConstraints;
    private File selectedFile;
    private JComboBox filterDropdown;
    private JComboBox processingTypeDropdown;

    //Componentes para parametros de filtro
    private JLabel paramLabel = new JLabel();
    private JSlider levelSlider;
    private File textureFile;
    private JButton textureButton;

    public MainFrame() {
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Filtrado de imagenes");   
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel,BorderLayout.NORTH);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        panelConstraints = new GridBagConstraints();
        inputPanel.setSize(500,400);

        //Label de selección de imagen
        JLabel fileLabel = new JLabel("Seleccione el archivo de imagen");
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 0;
        panelConstraints.ipady = 30;
        panelConstraints.ipadx = 30;
        inputPanel.add(fileLabel,panelConstraints);
        
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
        panelConstraints.gridx = 1;
        panelConstraints.ipady = 0;
        panelConstraints.ipadx = 0;
        inputPanel.add(fileButton,panelConstraints);  
        
        //Label de selección de filtro
        JLabel filterLabel = new JLabel("Seleccione el filtro a aplicar");
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 1;
        panelConstraints.ipady = 30;
        panelConstraints.ipadx = 30;
        inputPanel.add(filterLabel,panelConstraints);
        
        //Dropdown de opciones de filtros
        filterDropdown = new JComboBox(FILTER_LIST);
        filterDropdown.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedValue = MainFrame.this.filterDropdown.getSelectedItem().toString();
                MainFrame.this.showFilterParameter(selectedValue);
            }
        });
        panelConstraints.gridx = 1;
        panelConstraints.ipady = 0;
        panelConstraints.ipadx = 0;
        inputPanel.add(filterDropdown,panelConstraints);  

        //Label de selección de modalidad
        JLabel processingTypeLabel = new JLabel("Seleccione la modalidad de procesamiento");
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 2;
        panelConstraints.ipady = 30;
        panelConstraints.ipadx = 30;
        inputPanel.add(processingTypeLabel,panelConstraints);

        //Dropdown de secuencial o paralelo
        processingTypeDropdown = new JComboBox(PROC_TYPES);
        panelConstraints.gridx = 1;
        panelConstraints.ipady = 0;
        panelConstraints.ipadx = 0;
        inputPanel.add(processingTypeDropdown,panelConstraints);

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

        //Inicialización de componentes parametro
        levelSlider = new JSlider(0,100);
        levelSlider.setMinorTickSpacing(5);
        levelSlider.setMajorTickSpacing(20);
        levelSlider.setPaintTicks(true);
        levelSlider.setPaintLabels(true);

        JFileChooser textureFc = new JFileChooser();
        textureButton = new JButton("Examinar textura");
        textureButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == textureButton) {
                    int returnVal = textureFc.showOpenDialog(MainFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        MainFrame.this.textureFile = textureFc.getSelectedFile();
                        //This is where a real application would open the file.
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            }
        });

        this.setTitle("Main");
        this.setSize(800,600);
        this.setVisible(true);
    }

    private void showFilterParameter(String filterSelected){
        switch (filterSelected) {
            case "Opacidad":
                clearFilterParam();
                insertParamComponents(filterSelected);
                break;
            case "Ajuste de brillo":
                clearFilterParam();
                insertParamComponents(filterSelected);
                break;
            case "Textura":
                clearFilterParam();
                insertParamComponents(filterSelected);
                break;
            default:
                clearFilterParam();
                break;
        }
    }

    private void clearFilterParam(){
        inputPanel.remove(paramLabel);
        inputPanel.remove(levelSlider);
        inputPanel.remove(textureButton);

        inputPanel.revalidate();
    }

    private void insertParamComponents(String filterSelected){
        panelConstraints.gridx = 0;
        panelConstraints.gridy = 3;
        panelConstraints.ipady = 30;
        panelConstraints.ipadx = 30;

        if(filterSelected != "Textura"){
            paramLabel.setText("Seleccione el nivel de " + filterSelected );
            inputPanel.add(paramLabel,panelConstraints);
            
            panelConstraints.gridx = 1;
            panelConstraints.ipady = 0;
            panelConstraints.ipadx = 0;
            inputPanel.add(levelSlider,panelConstraints);
        }else{
            paramLabel.setText("Seleccione el archivo de textura");
            inputPanel.add(paramLabel,panelConstraints);

            panelConstraints.gridx = 1;
            panelConstraints.ipady = 0;
            panelConstraints.ipadx = 0;
            inputPanel.add(textureButton,panelConstraints);
        }

        inputPanel.revalidate();
    }

    private void invokeImageView(){
        String selectedFilter = String.valueOf(MainFrame.this.filterDropdown.getSelectedItem());
        String selectedOpMode = String.valueOf(MainFrame.this.processingTypeDropdown.getSelectedItem());
        File output = null;
        long elapsedMs = 0;
        switch(selectedFilter){
            case "Escala de Grises":
                GrayScale filter = new GrayScale(this.selectedFile, selectedOpMode);
                output = filter.getOutputFile();
                elapsedMs = filter.getElapsedMs();
                break;
            case "Ajuste de brillo":
                int factor = levelSlider.getValue();
                System.out.println("Factor de: " + factor);
                BrigthScale filter2 = new BrigthScale(this.selectedFile,factor,selectedOpMode);
                output = filter2.getOutputFile();
                elapsedMs = filter2.getElapsedMs();
                break;
            case "Sepia":
                SepiaScale filter3 = new SepiaScale(this.selectedFile, selectedOpMode);
                output = filter3.getOutputFile();
                elapsedMs = filter3.getElapsedMs();
                break;
            case "Color Invertido":
                InvertScale filter4 = new InvertScale(this.selectedFile, selectedOpMode);
                output = filter4.getOutputFile();
                elapsedMs = filter4.getElapsedMs();
                break;
        }
        ImageFrame imageView = new ImageFrame(output,elapsedMs);
    }
}