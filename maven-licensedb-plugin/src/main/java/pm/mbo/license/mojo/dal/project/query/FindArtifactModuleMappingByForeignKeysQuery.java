package pm.mbo.license.mojo.dal.project.query;

import pm.mbo.license.model.project.ArtifactModuleMapping;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindArtifactModuleMappingByForeignKeysQuery extends QueryDefinition<ArtifactModuleMapping> {

    private final ArtifactModuleMapping artifactModuleMapping;

    public FindArtifactModuleMappingByForeignKeysQuery(final ArtifactModuleMapping artifactModuleMapping) {
        this.artifactModuleMapping = artifactModuleMapping;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("artifact_id", artifactModuleMapping.getArtifact().getId());
        query.setParameter("module_id", artifactModuleMapping.getModule().getId());
    }

    @Override
    public ArtifactModuleMapping newEntry() {
        return artifactModuleMapping;
    }
}
