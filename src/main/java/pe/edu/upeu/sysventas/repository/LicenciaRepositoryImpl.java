package pe.edu.upeu.sysventas.repository;

import pe.edu.upeu.sysventas.model.Licencia;
import java.util.ArrayList;
import java.util.List;

public class LicenciaRepositoryImpl implements LicenciaRepository {
    private final List<Licencia> datos = new ArrayList<>();

    @Override
    public List<Licencia> findAll() {
        return new ArrayList<>(datos);
    }

    @Override
    public Licencia findByDni(String dni) {
        return datos.stream()
                .filter(l -> l.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Licencia save(Licencia licencia) {
        datos.add(licencia);
        return licencia;
    }

    @Override
    public Licencia update(String dniOriginal, Licencia licencia) {
        Licencia actual = findByDni(dniOriginal);
        if (actual == null) return null;

        actual.setDni(licencia.getDni());
        actual.setNombre(licencia.getNombre());
        actual.setFechaNacimiento(licencia.getFechaNacimiento());
        actual.setTipoLicencia(licencia.getTipoLicencia());
        actual.setFechaEmision(licencia.getFechaEmision());
        actual.setFechaVencimiento(licencia.getFechaVencimiento());
        actual.setTelefono(licencia.getTelefono());
        return actual;
    }

    @Override
    public void delete(String dni) {
        datos.removeIf(l -> l.getDni().equals(dni));
    }
}
