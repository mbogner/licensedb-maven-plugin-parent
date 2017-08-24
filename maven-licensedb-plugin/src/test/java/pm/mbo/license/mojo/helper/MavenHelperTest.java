package pm.mbo.license.mojo.helper;

import org.junit.Before;
import org.junit.Test;
import pm.mbo.license.model.artifact.Scope;
import pm.mbo.license.model.artifact.Type;
import pm.mbo.license.mojo.Data;
import pm.mbo.license.mojo.metadata.ArtifactMetadata;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class MavenHelperTest {

    private MavenHelper mavenHelper;

    @Before
    public void setUp() {
        mavenHelper = new MavenHelper();
    }

    @Test
    public void getMetadataForMavenProject() throws Exception {
        final ArtifactMetadata metadata = mavenHelper.getMetadataForMavenProject(Data.createMavenProject(), Data.createDependencies(Data.createMavenArtifact()));
        assertNotNull(metadata);
        assertThat(metadata.getScope(), equalTo(Scope.COMPILE));
        assertThat(metadata.getType(), equalTo(Type.JAR));
    }

    @Test(expected = IllegalStateException.class)
    public void getMetadataForMavenProject_noMetadata() throws Exception {
        mavenHelper.getMetadataForMavenProject(Data.createMavenProject(), Data.createDependencies());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMetadataForMavenProject_null() throws Exception {
        mavenHelper.getMetadataForMavenProject(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMetadataForMavenProject_null1() throws Exception {
        mavenHelper.getMetadataForMavenProject(Data.createMavenProject(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMetadataForMavenProject_null2() throws Exception {
        mavenHelper.getMetadataForMavenProject(null, Data.createDependencies(Data.createMavenArtifact()));
    }

    @Test
    public void artifactEqualsMavenProject() throws Exception {
        assertTrue(mavenHelper.artifactEqualsMavenProject(Data.createMavenArtifact(), Data.createMavenProject()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void artifactEqualsMavenProject_null() throws Exception {
        mavenHelper.artifactEqualsMavenProject(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void artifactEqualsMavenProject_null1() throws Exception {
        mavenHelper.artifactEqualsMavenProject(Data.createMavenArtifact(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void artifactEqualsMavenProject_null2() throws Exception {
        mavenHelper.artifactEqualsMavenProject(null, Data.createMavenProject());
    }

    @Test
    public void getArtifactCoordinates() throws Exception {
        assertThat(mavenHelper.getArtifactCoordinates(Data.createMavenArtifact()), equalTo("group:artifact:version"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getArtifactCoordinates_null() throws Exception {
        mavenHelper.getArtifactCoordinates(null);
    }

    @Test
    public void getMavenProjectCoordinates() throws Exception {
        assertThat(mavenHelper.getMavenProjectCoordinates(Data.createMavenProject()), equalTo("group:artifact:version"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getMavenProjectCoordinates_null() throws Exception {
        mavenHelper.getMavenProjectCoordinates(null);
    }

}