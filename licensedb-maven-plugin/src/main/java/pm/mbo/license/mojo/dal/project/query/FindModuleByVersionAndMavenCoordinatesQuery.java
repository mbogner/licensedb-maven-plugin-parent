package pm.mbo.license.mojo.dal.project.query;

import pm.mbo.license.model.project.Module;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindModuleByVersionAndMavenCoordinatesQuery extends QueryDefinition<Module> {

    private final Module module;

    public FindModuleByVersionAndMavenCoordinatesQuery(final Module module) {
        this.module = module;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("version_id", module.getVersion().getId());
        query.setParameter("maven_group_id", module.getMavenGroupId());
        query.setParameter("maven_artifact_id", module.getMavenArtifactId());
    }

    @Override
    public Module newEntry() {
        return module;
    }

    @Override
    public Class<Module> getResultClass() {
        return Module.class;
    }
}
