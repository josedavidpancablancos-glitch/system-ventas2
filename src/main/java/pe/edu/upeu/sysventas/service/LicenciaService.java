package pe.edu.upeu.sysventas.service;

import pe.edu.upeu.sysventas.model.Licencia;
import java.util.List;

public interface LicenciaService {
    List<Licencia> listar();
    Licencia buscarPorDni(String dni);
    Licencia registrar(Licencia licencia);
    Licencia modificar(String dniOriginal, Licencia licencia);
    void eliminar(String dni);
}
