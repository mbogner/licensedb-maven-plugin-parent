package pm.mbo.license.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import pm.mbo.license.model.artifact.Scope;
import pm.mbo.license.model.artifact.Type;
import pm.mbo.license.mojo.metadata.ArtifactMetadata;

import java.util.Set;

public final class MavenUtil {

    public static ArtifactMetadata getMetadataForMavenProject(final MavenProject mavenProject, final Set<Artifact> dependencies) {
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

    public static boolean artifactEqualsMavenProject(final Artifact artifact, final MavenProject mavenProject) {
        return getArtifactCoordinates(artifact).equals(getMavenProjectCoordinates(mavenProject));
    }

    public static String getArtifactCoordinates(final Artifact obj) {
        return String.format("%s:%s:%s", obj.getGroupId(), obj.getArtifactId(), obj.getVersion());
    }

    public static String getMavenProjectCoordinates(final MavenProject obj) {
        return String.format("%s:%s:%s", obj.getGroupId(), obj.getArtifactId(), obj.getVersion());
    }

    private MavenUtil() {
        throw new IllegalAccessError();
    }
}
