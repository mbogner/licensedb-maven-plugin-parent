package pm.mbo.license.mojo.dal.artifact.query;

import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.mojo.dal.QueryDefinition;

import javax.persistence.Query;

public class FindArtifactByMavenCoordinatesQuery extends QueryDefinition<Artifact> {

    private final Artifact artifact;

    public FindArtifactByMavenCoordinatesQuery(final Artifact artifact) {
        this.artifact = artifact;
    }

    @Override
    public void setParameters(final Query query) {
        query.setParameter("coordinates", artifact.getMavenCoordinates());
    }

    @Override
    public Artifact newEntry() {
        return artifact;
    }
}
