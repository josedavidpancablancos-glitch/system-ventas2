package pe.edu.upeu.sysventas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pe.edu.upeu.sysventas.model.Licencia;
import pe.edu.upeu.sysventas.service.LicenciaService;
import pe.edu.upeu.sysventas.service.impl.LicenciaServiceImpl;

import java.time.LocalDate;

public class LicenciaController {
    @FXML private TextField txtDni, txtNombre, txtTelefono, txtFiltro;
    @FXML private DatePicker dpNacimiento, dpEmision, dpVencimiento;
    @FXML private ComboBox<String> cbxTipoLicencia;
    @FXML private TableView<Licencia> tableView;
    @FXML private TableColumn<Licencia, String> colDni, colNombre, colTipo, colTelefono, colEstado;
    @FXML private TableColumn<Licencia, LocalDate> colNacimiento, colEmision, colVencimiento;

    private final LicenciaService service = new LicenciaServiceImpl();
    private final ObservableList<Licencia> datos = FXCollections.observableArrayList();
    private String dniOriginal = null;

    @FXML
    public void initialize() {
        cbxTipoLicencia.setItems(FXCollections.observableArrayList(
                "A-I", "A-IIa", "A-IIb", "A-IIIa", "A-IIIb", "A-IIIc"));

        colDni.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDni()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colNacimiento.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getFechaNacimiento()));
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipoLicencia()));
        colEmision.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getFechaEmision()));
        colVencimiento.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getFechaVencimiento()));
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstado()));

        tableView.setItems(datos);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, old, seleccionado) -> {
            if (seleccionado != null) cargarFormulario(seleccionado);
        });
        listar();
    }

    @FXML
    private void registrar() {
        if (!validar()) return;
        if (service.buscarPorDni(txtDni.getText().trim()) != null) {
            mensaje(Alert.AlertType.ERROR, "Error", "El DNI ya está registrado.");
            return;
        }
        service.registrar(leerFormulario());
        listar();
        limpiar();
        mensaje(Alert.AlertType.INFORMATION, "Registro", "Licencia registrada correctamente.");
    }

    @FXML
    private void modificar() {
        if (dniOriginal == null) {
            mensaje(Alert.AlertType.WARNING, "Aviso", "Seleccione un registro para modificar.");
            return;
        }
        if (!validar()) return;
        if (!dniOriginal.equals(txtDni.getText().trim()) && service.buscarPorDni(txtDni.getText().trim()) != null) {
            mensaje(Alert.AlertType.ERROR, "Error", "El nuevo DNI ya está registrado.");
            return;
        }
        service.modificar(dniOriginal, leerFormulario());
        listar();
        limpiar();
        mensaje(Alert.AlertType.INFORMATION, "Modificación", "Registro modificado correctamente.");
    }

    @FXML
    private void eliminar() {
        Licencia seleccionada = tableView.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mensaje(Alert.AlertType.WARNING, "Aviso", "Seleccione un registro para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Desea eliminar el DNI " + seleccionada.getDni() + "?", ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirmar eliminación");
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            service.eliminar(seleccionada.getDni());
            listar();
            limpiar();
        }
    }

    @FXML
    private void limpiar() {
        txtDni.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtFiltro.clear();
        dpNacimiento.setValue(null);
        dpEmision.setValue(null);
        dpVencimiento.setValue(null);
        cbxTipoLicencia.setValue(null);
        dniOriginal = null;
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    private void buscar() {
        String filtro = txtFiltro.getText().trim().toLowerCase();
        if (filtro.isEmpty()) {
            listar();
            return;
        }
        datos.setAll(service.listar().stream()
                .filter(l -> l.getDni().toLowerCase().contains(filtro) || l.getNombre().toLowerCase().contains(filtro))
                .toList());
    }

    private void listar() { datos.setAll(service.listar()); }

    private Licencia leerFormulario() {
        return new Licencia(txtDni.getText().trim(), txtNombre.getText().trim(),
                dpNacimiento.getValue(), cbxTipoLicencia.getValue(),
                dpEmision.getValue(), dpVencimiento.getValue(), txtTelefono.getText().trim());
    }

    private void cargarFormulario(Licencia l) {
        dniOriginal = l.getDni();
        txtDni.setText(l.getDni());
        txtNombre.setText(l.getNombre());
        dpNacimiento.setValue(l.getFechaNacimiento());
        cbxTipoLicencia.setValue(l.getTipoLicencia());
        dpEmision.setValue(l.getFechaEmision());
        dpVencimiento.setValue(l.getFechaVencimiento());
        txtTelefono.setText(l.getTelefono());
    }

    private boolean validar() {
        if (!txtDni.getText().trim().matches("\\d{8}")) {
            mensaje(Alert.AlertType.ERROR, "Error", "El DNI debe tener 8 números."); return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            mensaje(Alert.AlertType.ERROR, "Error", "Ingrese el nombre completo."); return false;
        }
        if (dpNacimiento.getValue() == null || cbxTipoLicencia.getValue() == null || dpEmision.getValue() == null || dpVencimiento.getValue() == null) {
            mensaje(Alert.AlertType.ERROR, "Error", "Complete las fechas y el tipo de licencia."); return false;
        }
        if (dpVencimiento.getValue().isBefore(dpEmision.getValue())) {
            mensaje(Alert.AlertType.ERROR, "Error", "La fecha de vencimiento no puede ser anterior a la emisión."); return false;
        }
        if (!txtTelefono.getText().trim().matches("\\d{9}")) {
            mensaje(Alert.AlertType.ERROR, "Error", "El teléfono debe tener 9 números."); return false;
        }
        return true;
    }

    private void mensaje(Alert.AlertType tipo, String titulo, String texto) {
        Alert a = new Alert(tipo);
        a.setTitle(titulo); a.setHeaderText(null); a.setContentText(texto); a.showAndWait();
    }
}
