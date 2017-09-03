package pm.mbo;

import hudson.util.ArgumentListBuilder;

public class CommandBuilder {

    public ArgumentListBuilder createCommandFrom(final LicensePlugin plugin) {
        if (null == plugin) {
            throw new IllegalArgumentException("plugin must not be null");
        }

        final ArgumentListBuilder cmd = new ArgumentListBuilder();
        cmd.add(plugin.getDescriptor().getMavenBin());
        addValueIfNotEmpty(cmd, "-D", "licensedb.projectId", plugin.getProjectId(), false);
        addValueIfNotEmpty(cmd, "-D", "licensedb.projectName", plugin.getName(), false);
        addValueIfNotEmpty(cmd, "-D", "licensedb.projectComponent", plugin.getComponent(), false);
        addGlobalIfNoLocal(cmd, "-D", "licensedb.timeout", plugin.getTimeout(), plugin.getDescriptor().getTimeout(), false);

        addValueIfNotEmpty(cmd, "-D", "licensedb.databaseUrl", plugin.getDescriptor().getDbUrl(), false);
        addValueIfNotEmpty(cmd, "-D", "licensedb.databaseUser", plugin.getDescriptor().getDbUser(), true);
        addValueIfNotEmpty(cmd, "-D", "licensedb.databasePassword", plugin.getDescriptor().getDbPass(), true);
        addValueIfNotEmpty(cmd, "-D", "licensedb.databaseDriverClass", plugin.getDescriptor().getDbDriver(), false);
        addValueIfNotEmpty(cmd, "-D", "licensedb.databaseDialect", plugin.getDescriptor().getDbDialect(), false);
        addValueIfNotEmpty(cmd, "-D", "licensedb.databaseHbm2ddl", plugin.getDescriptor().getDbGeneration(), false);

        addGlobalIfNoLocal(cmd, "-D", "licensedb.dryRun", plugin.getDryRun(), plugin.getDescriptor().getSkip(), false);
        addGlobalIfNoLocal(cmd, "-D", "licensedb.skip", plugin.getSkip(), plugin.getDescriptor().getSkip(), false);

        addCustom(cmd, plugin);

        cmd.addTokenized(plugin.getDescriptor().getMavenGoal());
        return cmd;
    }

    protected void addCustom(final ArgumentListBuilder cmd, final LicensePlugin plugin) {
        // do nothing
    }

    protected void addValueIfNotEmpty(final ArgumentListBuilder cmd, final String prefix, final String key, final String value, final boolean mask) {
        if (null != value && value.length() > 0) {
            cmd.addKeyValuePair(prefix, key, value, mask);
        }
    }

    protected void addGlobalIfNoLocal(final ArgumentListBuilder cmd, final String prefix, final String key, final Object localValue, final Object globalValue, final boolean mask) {
        if (localValue == null) {
            if (null != globalValue) {
                addValueIfNotEmpty(cmd, prefix, key, String.valueOf(globalValue), mask);
            }
        } else {
            addValueIfNotEmpty(cmd, prefix, key, String.valueOf(localValue), mask);
        }
    }

}
