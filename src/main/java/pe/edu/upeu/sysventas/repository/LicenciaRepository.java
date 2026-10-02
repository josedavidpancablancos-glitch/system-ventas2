package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Licencia;
import java.util.List;

public interface LicenciaRepository {
    List<Licencia> findAll();
    Licencia findByDni(String dni);
    Licencia save(Licencia licencia);
    Licencia update(String dniOriginal, Licencia licencia);
    void delete(String dni);
}
