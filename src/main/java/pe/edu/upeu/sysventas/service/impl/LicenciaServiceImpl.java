package pe.edu.upeu.sysventas.service.impl;

import pe.edu.upeu.sysventas.model.Licencia;
import pe.edu.upeu.sysventas.repository.LicenciaRepository;
import pe.edu.upeu.sysventas.repository.LicenciaRepositoryImpl;
import pe.edu.upeu.sysventas.service.LicenciaService;
import java.util.List;

public class LicenciaServiceImpl implements LicenciaService {
    private final LicenciaRepository repository = new LicenciaRepositoryImpl();

    @Override
    public List<Licencia> listar() { return repository.findAll(); }

    @Override
    public Licencia buscarPorDni(String dni) { return repository.findByDni(dni); }

    @Override
    public Licencia registrar(Licencia licencia) { return repository.save(licencia); }

    @Override
    public Licencia modificar(String dniOriginal, Licencia licencia) {
        return repository.update(dniOriginal, licencia);
    }

    @Override
    public void eliminar(String dni) { repository.delete(dni); }
}
