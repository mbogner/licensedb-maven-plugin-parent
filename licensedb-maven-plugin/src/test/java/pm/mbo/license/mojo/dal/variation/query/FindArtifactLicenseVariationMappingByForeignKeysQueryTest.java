package pm.mbo.license.mojo.dal.variation.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.artifact.Artifact;
import pm.mbo.license.model.variation.ArtifactLicenseVariationMapping;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DatabaseTest;

public class FindArtifactLicenseVariationMappingByForeignKeysQueryTest
        extends QueryDefinitionTest<Long, ArtifactLicenseVariationMapping, ArtifactLicenseVariationMapping, FindArtifactLicenseVariationMappingByForeignKeysQuery> {

    private static int databasePort;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        databasePort = QueryDefinitionTest.initDatabaseTest();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        DatabaseTest.shutdownDatabaseTest(databasePort);
    }

    @Override
    protected FindArtifactLicenseVariationMappingByForeignKeysQuery loadQueryDefinition() {
        final ArtifactLicenseVariationMapping mapping = new ArtifactLicenseVariationMapping();

        mapping.setArtifact(new Artifact());
        mapping.getArtifact().setId(-1L);

        mapping.setLicenseVariation(new LicenseVariation());
        mapping.getLicenseVariation().setId(-1L);

        return new FindArtifactLicenseVariationMappingByForeignKeysQuery(mapping);
    }
}