package pm.mbo.license.mojo.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMetadata {

    private String projectId;
    private String projectName;
    private String projectComponent;

}
