package pm.mbo.license.mojo.dal.project.query;

import pm.mbo.license.model.project.Module;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class RemoveAllArtifactModuleMappingsOfModuleQuery extends QueryDefinition<Integer> {

    private final Module module;

    public RemoveAllArtifactModuleMappingsOfModuleQuery(final Module module) {
        this.module = module;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("module_id", module.getId());
    }

    @Override
    public Class<Integer> getResultClass() {
        return Integer.class;
    }
}
