package pm.mbo.license.mojo.dal.project;

import org.apache.maven.plugin.logging.Log;
import pm.mbo.license.model.project.Module;
import pm.mbo.license.mojo.dal.AbstractRepository;
import pm.mbo.license.mojo.dal.EntityManagerDelegate;

public class ModuleRepository extends AbstractRepository<Long, Module> {

    public ModuleRepository(final Log log, final EntityManagerDelegate em) {
        super(log, em);
    }

    @Override
    protected Class<Module> getManagedClass() {
        return Module.class;
    }
}
