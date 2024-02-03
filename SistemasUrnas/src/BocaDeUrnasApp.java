import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BocaDeUrnasApp {

    private static List<Candidato> candidatos = new ArrayList<>();
    private static Map<String, Map<String, Map<String, Integer>>> resultados = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BocaDeUrnasForm bocaDeUrnasForm = new BocaDeUrnasForm();
            bocaDeUrnasForm.mostrarFormulario();
        });
    }

    static List<Candidato> getCandidatos() {
        return candidatos;
    }

    static void agregarCandidato(Candidato candidato) {
        candidatos.add(candidato);
    }

    static void registrarVoto(String provincia, String ciudad, String candidato) {
        resultados
                .computeIfAbsent(provincia, k -> new HashMap<>())
                .computeIfAbsent(ciudad, k -> new HashMap<>())
                .merge(candidato, 1, Integer::sum);
    }

    static Map<String, Map<String, Map<String, Integer>>> getResultados() {
        return resultados;
    }
}

class Candidato {
    private String nombre;
    private String partido;

    public Candidato(String nombre, String partido) {
        this.nombre = nombre;
        this.partido = partido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPartido() {
        return partido;
    }
}

class BocaDeUrnasForm {

    private JFrame frame;

    public BocaDeUrnasForm() {
    }

    public void mostrarFormulario() {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Boca de Urnas");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(crearPanelPrincipal());
            frame.setSize(800, 400);
            frame.setLocationRelativeTo(null);

            configurarInterfaz();

            frame.setVisible(true);
        });
    }

    private void configurarInterfaz() {
        JMenuBar menuBar = new JMenuBar();

        JMenu archivoMenu = new JMenu("Archivo");

        JMenuItem salirItem = new JMenuItem("Salir");
        salirItem.addActionListener(new Salir());
        archivoMenu.add(salirItem);
        menuBar.add(archivoMenu);

        JMenuItem candidatosItem = new JMenuItem("Candidatos");
        JMenuItem bocaDeUrnaItem = new JMenuItem("Proceso");
        JMenuItem reportesItem = new JMenuItem("Reportes");

        candidatosItem.addActionListener(new CandidatosForm());
        bocaDeUrnaItem.addActionListener(new BocaDeUrnaForm());
        reportesItem.addActionListener(new ReportesForm());

        JMenu espacioMenu = new JMenu("");
        espacioMenu.setEnabled(false);
        menuBar.add(espacioMenu);

        menuBar.add(candidatosItem);
        menuBar.add(bocaDeUrnaItem);
        menuBar.add(reportesItem);

        frame.setJMenuBar(menuBar);
    }

    private JPanel crearPanelPrincipal() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        return panel;
    }
}

class Salir implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}

class CandidatosForm implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame candidatosFrame = new JFrame("Formulario de Candidatos");
        candidatosFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        candidatosFrame.setContentPane(new CandidatosPanel());
        candidatosFrame.setSize(400, 200);
        candidatosFrame.setLocationRelativeTo(null);
        candidatosFrame.setVisible(true);
    }
}

class BocaDeUrnaForm implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame bocaDeUrnaFrame = new JFrame("Boca de Urna - Seleccionar Provincia y Ciudad");
        bocaDeUrnaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bocaDeUrnaFrame.setContentPane(new BocaDeUrnaPanel());
        bocaDeUrnaFrame.setSize(400, 200);
        bocaDeUrnaFrame.setLocationRelativeTo(null);
        bocaDeUrnaFrame.setVisible(true);
    }
}

class ReportesForm implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JFrame reportesFrame = new JFrame("Formulario de Reportes");
        reportesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportesFrame.setContentPane(new ReportesPanel());
        reportesFrame.setSize(400, 200);
        reportesFrame.setLocationRelativeTo(null);
        reportesFrame.setVisible(true);
    }
}

class CandidatosPanel extends JPanel {
    private JTextField nombreField;
    private JTextField partidoField;
    private JButton guardarButton;

    public CandidatosPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel nombreLabel = new JLabel("Nombre:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(nombreLabel, constraints);

        nombreField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(nombreField, constraints);

        JLabel partidoLabel = new JLabel("Partido:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(partidoLabel, constraints);

        partidoField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(partidoField, constraints);

        guardarButton = new JButton("Guardar");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        add(guardarButton, constraints);

        guardarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                String partido = partidoField.getText();
                BocaDeUrnasApp.agregarCandidato(new Candidato(nombre, partido));
                JOptionPane.showMessageDialog(null, "Candidato guardado:\nNombre: " + nombre + "\nPartido: " + partido);
            }
        });
    }
}

class BocaDeUrnaPanel extends JPanel {
    private JComboBox<String> provinciasComboBox;
    private JComboBox<String> ciudadesComboBox;
    private JComboBox<String> candidatosComboBox;
    private JButton votarButton;

    public BocaDeUrnaPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel provinciaLabel = new JLabel("Provincia:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(provinciaLabel, constraints);

        provinciasComboBox = new JComboBox<>(new String[]{"Provincia 1", "Provincia 2", "Provincia 3"});
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        add(provinciasComboBox, constraints);

        JLabel ciudadLabel = new JLabel("Ciudad:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(ciudadLabel, constraints);

        ciudadesComboBox = new JComboBox<>(new String[]{"Ciudad 1", "Ciudad 2", "Ciudad 3"});
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        add(ciudadesComboBox, constraints);

        JLabel candidatoLabel = new JLabel("Candidato:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(candidatoLabel, constraints);

        candidatosComboBox = new JComboBox<>(obtenerNombresCandidatos());
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        add(candidatosComboBox, constraints);

        votarButton = new JButton("Votar");
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        add(votarButton, constraints);

        votarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String provincia = (String) provinciasComboBox.getSelectedItem();
                String ciudad = (String) ciudadesComboBox.getSelectedItem();
                String candidato = (String) candidatosComboBox.getSelectedItem();

                // Registrar el voto
                BocaDeUrnasApp.registrarVoto(provincia, ciudad, candidato);

                JOptionPane.showMessageDialog(null, "Voto registrado:\nProvincia: " + provincia + "\nCiudad: " + ciudad + "\nCandidato: " + candidato);
            }
        });
    }

    private String[] obtenerNombresCandidatos() {
        List<Candidato> candidatos = BocaDeUrnasApp.getCandidatos();
        String[] nombres = new String[candidatos.size()];
        for (int i = 0; i < candidatos.size(); i++) {
            nombres[i] = candidatos.get(i).getNombre();
        }
        return nombres;
    }
}

class ReportesPanel extends JPanel {
    public ReportesPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);

        JButton resultadosProvinciaButton = new JButton("Resultados por Provincia");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(resultadosProvinciaButton, constraints);

        JButton resultadosCiudadButton = new JButton("Resultados por Ciudad");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(resultadosCiudadButton, constraints);

        JButton resultadoFinalButton = new JButton("Resultado Final");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(resultadoFinalButton, constraints);

        resultadosProvinciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame resultadosProvinciaFrame = new JFrame("Resultados por Provincia");
                resultadosProvinciaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultadosProvinciaFrame.setContentPane(new ResultadosProvinciaPanel());
                resultadosProvinciaFrame.setSize(400, 200);
                resultadosProvinciaFrame.setLocationRelativeTo(null);
                resultadosProvinciaFrame.setVisible(true);
            }
        });

        resultadosCiudadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame resultadosCiudadFrame = new JFrame("Resultados por Ciudad");
                resultadosCiudadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultadosCiudadFrame.setContentPane(new ResultadosCiudadPanel());
                resultadosCiudadFrame.setSize(400, 200);
                resultadosCiudadFrame.setLocationRelativeTo(null);
                resultadosCiudadFrame.setVisible(true);
            }
        });

        resultadoFinalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame resultadoFinalFrame = new JFrame("Resultado Final");
                resultadoFinalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                resultadoFinalFrame.setContentPane(new ResultadoFinalPanel());
                resultadoFinalFrame.setSize(400, 200);
                resultadoFinalFrame.setLocationRelativeTo(null);
                resultadoFinalFrame.setVisible(true);
            }
        });
    }
}

class ResultadosProvinciaPanel extends JPanel {
    public ResultadosProvinciaPanel() {
        setLayout(new BorderLayout());

        JTextArea resultadosArea = new JTextArea(obtenerResultadosPorProvincia());
        resultadosArea.setEditable(false);
        add(new JScrollPane(resultadosArea), BorderLayout.CENTER);
    }

    private String obtenerResultadosPorProvincia() {
        StringBuilder resultados = new StringBuilder("Resultados por Provincia:\n");

        for (String provincia : BocaDeUrnasApp.getResultados().keySet()) {
            resultados.append(provincia).append(":\n");

            for (String ciudad : BocaDeUrnasApp.getResultados().get(provincia).keySet()) {
                resultados.append("  ").append(ciudad).append(":\n");

                for (String candidato : BocaDeUrnasApp.getResultados().get(provincia).get(ciudad).keySet()) {
                    int votos = BocaDeUrnasApp.getResultados().get(provincia).get(ciudad).get(candidato);
                    resultados.append("    ").append(candidato).append(": ").append(votos).append(" votos\n");
                }
            }
        }

        return resultados.toString();
    }
}

class ResultadosCiudadPanel extends JPanel {
    public ResultadosCiudadPanel() {
        setLayout(new BorderLayout());

        JTextArea resultadosArea = new JTextArea(obtenerResultadosPorCiudad());
        resultadosArea.setEditable(false);
        add(new JScrollPane(resultadosArea), BorderLayout.CENTER);
    }

    private String obtenerResultadosPorCiudad() {
        StringBuilder resultados = new StringBuilder("Resultados por Ciudad:\n");

        for (String provincia : BocaDeUrnasApp.getResultados().keySet()) {
            for (String ciudad : BocaDeUrnasApp.getResultados().get(provincia).keySet()) {
                resultados.append(ciudad).append(":\n");

                for (String candidato : BocaDeUrnasApp.getResultados().get(provincia).get(ciudad).keySet()) {
                    int votos = BocaDeUrnasApp.getResultados().get(provincia).get(ciudad).get(candidato);
                    resultados.append("    ").append(candidato).append(": ").append(votos).append(" votos\n");
                }
            }
        }

        return resultados.toString();
    }
}

class ResultadoFinalPanel extends JPanel {
    public ResultadoFinalPanel() {
        setLayout(new BorderLayout());

        JTextArea resultadosArea = new JTextArea(obtenerResultadoFinal());
        resultadosArea.setEditable(false);
        add(new JScrollPane(resultadosArea), BorderLayout.CENTER);
    }

    private String obtenerResultadoFinal() {
        StringBuilder resultados = new StringBuilder("Resultado Final:\n");

        for (String provincia : BocaDeUrnasApp.getResultados().keySet()) {
            resultados.append(provincia).append(":\n");

            for (String ciudad : BocaDeUrnasApp.getResultados().get(provincia).keySet()) {
                resultados.append("  ").append(ciudad).append(":\n");

                for (String candidato : BocaDeUrnasApp.getResultados().get(provincia).get(ciudad).keySet()) {
                    int votos = BocaDeUrnasApp.getResultados().get(provincia).get(ciudad).get(candidato);
                    resultados.append("    ").append(candidato).append(": ").append(votos).append(" votos\n");
                }
            }
        }

        return resultados.toString();
    }
}
