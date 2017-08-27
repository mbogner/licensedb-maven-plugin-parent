package pm.mbo.license.model.project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModuleTest {

    @Test(expected = IllegalStateException.class)
    public void createFullCoordinates_NullVersion() throws Exception {
        new Module().createFullCoordinates();
    }

    @Test(expected = IllegalStateException.class)
    public void createFullCoordinates_NullProject() throws Exception {
        final Module module = new Module();
        module.setVersion(new Version());
        module.createFullCoordinates();
    }

    @Test
    public void createFullCoordinates() throws Exception {
        final Module module = new Module();
        module.setVersion(new Version());
        module.getVersion().setProject(new Project());
        final String fullCoordinates = module.createFullCoordinates();
        assertEquals("null:null:null:null", fullCoordinates);
    }

}