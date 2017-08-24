package pm.mbo.license.mojo.metadata;

import lombok.Data;
import pm.mbo.license.model.artifact.Scope;
import pm.mbo.license.model.artifact.Type;

@Data
public class ArtifactMetadata {

    private Scope scope;
    private Type type;

}
