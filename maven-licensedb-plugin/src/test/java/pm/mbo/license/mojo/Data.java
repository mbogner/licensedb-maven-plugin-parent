package pm.mbo.license.mojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Data {

    public static MavenProject createMavenProject() {
        final MavenProject mavenProject = new MavenProject();
        mavenProject.setGroupId("group");
        mavenProject.setArtifactId("artifact");
        mavenProject.setVersion("version");
        return mavenProject;
    }

    public static Artifact createMavenArtifact() {
        final Artifact artifact = Mockito.mock(Artifact.class);
        Mockito.when(artifact.getGroupId()).thenReturn("group");
        Mockito.when(artifact.getArtifactId()).thenReturn("artifact");
        Mockito.when(artifact.getVersion()).thenReturn("version");
        Mockito.when(artifact.getScope()).thenReturn("compile");
        Mockito.when(artifact.getType()).thenReturn("jar");
        return artifact;
    }

    public static Set<Artifact> createDependencies(final Artifact... artifacts) {
        return new HashSet<>(Arrays.asList(artifacts));
    }

}
