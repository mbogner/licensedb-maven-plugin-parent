package pm.mbo.license.mojo.helper;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;
import pm.mbo.license.model.artifact.Scope;
import pm.mbo.license.model.artifact.Type;
import pm.mbo.license.mojo.metadata.ArtifactMetadata;

import java.util.Set;

@Component(role = MavenHelper.class, hint = "default")
public class MavenHelper {

    public ArtifactMetadata getMetadataForMavenProject(final MavenProject mavenProject, final Set<Artifact> dependencies) {
        if(null == mavenProject || null == dependencies) {
            throw new IllegalArgumentException("mavenProject and dependencies must not be null");
        }
        for (final Artifact artifact : dependencies) {
            if (artifactEqualsMavenProject(artifact, mavenProject)) {
                final ArtifactMetadata metadata = new ArtifactMetadata();
                metadata.setScope(Scope.valueOf(artifact.getScope().toUpperCase()));
                metadata.setType(Type.valueOf(artifact.getType().toUpperCase()));
                return metadata;
            }
        }
        throw new IllegalStateException("no scope for dependency " + getMavenProjectCoordinates(mavenProject));
    }

    public boolean artifactEqualsMavenProject(final Artifact artifact, final MavenProject mavenProject) {
        return getArtifactCoordinates(artifact).equals(getMavenProjectCoordinates(mavenProject));
    }

    public String getArtifactCoordinates(final Artifact obj) {
        if(null == obj) {
            throw new IllegalArgumentException("artifact must not be null");
        }
        return String.format("%s:%s:%s", obj.getGroupId(), obj.getArtifactId(), obj.getVersion());
    }

    public String getMavenProjectCoordinates(final MavenProject obj) {
        if(null == obj) {
            throw new IllegalArgumentException("mavenproject must not be null");
        }
        return String.format("%s:%s:%s", obj.getGroupId(), obj.getArtifactId(), obj.getVersion());
    }

}
