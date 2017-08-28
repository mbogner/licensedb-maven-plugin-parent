package pm.mbo.license.mojo.dal.variation.query;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import pm.mbo.license.model.variation.LicenseVariation;
import pm.mbo.license.mojo.dal.QueryDefinitionTest;
import pm.mbo.license.test.DataGenerator;
import pm.mbo.license.test.DatabaseTest;

public class FindLicenseVariationByNameQueryTest
        extends QueryDefinitionTest<Long, LicenseVariation, LicenseVariation, FindLicenseVariationByNameQuery> {

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
    protected FindLicenseVariationByNameQuery loadQueryDefinition() {
        final LicenseVariation licenseVariation = new LicenseVariation();
        licenseVariation.setName(DataGenerator.createRandomString(10));
        return new FindLicenseVariationByNameQuery(licenseVariation);
    }

}