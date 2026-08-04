package com.udb.inventario.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {

    // ===== ATRIBUTOS: campos del formulario =====
    private JTextField campoCodigo;
    private JTextField campoMarca;
    private JTextField campoRam;
    private JComboBox<String> campoTipo;
    private JComboBox<String> campoEstado;

    // ===== ATRIBUTOS: tabla =====
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // ===== ATRIBUTOS: parte inferior =====
    private JLabel lblFilaSeleccionada;
    private JButton btnGuardar;
    private JButton btnDarBaja;

    public VentanaPrincipal() {
        setTitle("Gestión de Inventario TI UDB");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(construirPanelFormulario(), BorderLayout.NORTH);
        add(construirPanelTabla(), BorderLayout.CENTER);
        add(construirPanelInferior(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ==========================================================
    // FORMULARIO (arriba)
    // ==========================================================
    private JPanel construirPanelFormulario() {
        // --- Grupo Codigo ---
        JLabel lblCodigo = new JLabel("Código de inventario");
        campoCodigo = new JTextField();
        JPanel grupoCodigo = new JPanel(new GridLayout(2, 1));
        grupoCodigo.add(lblCodigo);
        grupoCodigo.add(campoCodigo);

        // --- Grupo Tipo (JComboBox) ---
        JLabel lblTipo = new JLabel("Tipo de equipo");
        String[] opcionesTipo = {"Laptop", "Proyector"};
        campoTipo = new JComboBox<>(opcionesTipo);
        JPanel grupoTipo = new JPanel(new GridLayout(2, 1));
        grupoTipo.add(lblTipo);
        grupoTipo.add(campoTipo);

        //  Grupo Marca
        JLabel lblMarca = new JLabel("Marca");
        campoMarca = new JTextField();
        JPanel grupoMarca = new JPanel(new GridLayout(2, 1));
        grupoMarca.add(lblMarca);
        grupoMarca.add(campoMarca);

        //  Grupo Ram
        JLabel lblRam = new JLabel("RAM (GB)");
        campoRam = new JTextField();
        JPanel grupoRam = new JPanel(new GridLayout(2, 1));
        grupoRam.add(lblRam);
        grupoRam.add(campoRam);

        //  Grupo Estado (JComboBox)
        JLabel lblEstado = new JLabel("Estado");
        String[] opcionesEstado = {"Activo", "En Reparación"};
        campoEstado = new JComboBox<>(opcionesEstado);
        JPanel grupoEstado = new JPanel(new GridLayout(2, 1));
        grupoEstado.add(lblEstado);
        grupoEstado.add(campoEstado);

        //  Boton Guardar
        btnGuardar = new JButton("Guardar equipo");

        //  Panel grande que junta todo (0 filas = automático, 2 columnas)
        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelFormulario.add(grupoCodigo);
        panelFormulario.add(grupoTipo);
        panelFormulario.add(grupoMarca);
        panelFormulario.add(grupoRam);
        panelFormulario.add(grupoEstado);
        panelFormulario.add(btnGuardar);

        return panelFormulario;
    }

    // ==========================================================
    // TABLA (centro)
    // ==========================================================
    private JScrollPane construirPanelTabla() {
        String[] columnas = {"Código", "Tipo", "Marca", "RAM", "Estado"};

        // El "0" indica que la tabla arranca sin filas (se llenan después)
        modeloTabla = new DefaultTableModel(columnas, 0) {
            // Esto evita que el usuario edite las celdas directo con doble clic
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        // Cuando el usuario selecciona una fila, actualizamos el label de abajo
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                Object codigoFila = tabla.getValueAt(tabla.getSelectedRow(), 0);
                lblFilaSeleccionada.setText("Fila seleccionada: " + codigoFila);
            }
        });

        // JScrollPane envuelve la tabla para que aparezca scroll si hay muchas filas
        return new JScrollPane(tabla);
    }


    // PARTE INFERIOR (fila seleccionada + botón dar de baja)
    private JPanel construirPanelInferior() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblFilaSeleccionada = new JLabel("Fila seleccionada: ninguna");
        btnDarBaja = new JButton("Dar de baja");

        panelInferior.add(lblFilaSeleccionada, BorderLayout.WEST);
        panelInferior.add(btnDarBaja, BorderLayout.EAST);

        return panelInferior;
    }


    // GETTERS: para que el Controller lea lo que el usuario escribio

    public String getCodigoIngresado() {
        return campoCodigo.getText();
    }

    public String getMarcaIngresada() {
        return campoMarca.getText();
    }

    public String getRamIngresada() {
        return campoRam.getText();
    }

    public String getTipoSeleccionado() {
        return (String) campoTipo.getSelectedItem();
    }

    public String getEstadoSeleccionado() {
        return (String) campoEstado.getSelectedItem();
    }

    // Devuelve el código de la fila seleccionada en la tabla (o null si no hay ninguna)
    public String getCodigoFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return null;
        }
        return (String) tabla.getValueAt(fila, 0);
    }

    // METODOS para que el Controller "escuche" los botones

    public void addListenerGuardar(ActionListener listener) {
        btnGuardar.addActionListener(listener);
    }

    public void addListenerDarBaja(ActionListener listener) {
        btnDarBaja.addActionListener(listener);
    }

    // METODOS para que el Controller actualice la tabla y el formulario

    // Agrega una fila nueva a la tabla (el Controller llama esto tras guardar)
    public void agregarFilaTabla(String codigo, String tipo, String marca, String ram, String estado) {
        modeloTabla.addRow(new Object[]{codigo, tipo, marca, ram, estado});
    }

    // Elimina la fila actualmente seleccionada (el Controller llama esto tras dar de baja)
    public void eliminarFilaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila != -1) {
            modeloTabla.removeRow(fila);
            lblFilaSeleccionada.setText("Fila seleccionada: ninguna");
        }
    }

    // Limpia los campos del formulario (el Controller llama esto tras guardar)
    public void limpiarFormulario() {
        campoCodigo.setText("");
        campoMarca.setText("");
        campoRam.setText("");
        campoTipo.setSelectedIndex(0);
        campoEstado.setSelectedIndex(0);
    }

    // Muestra un mensaje de error o éxito en un cuadro emergente
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}