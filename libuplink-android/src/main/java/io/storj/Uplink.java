package io.storj;

/**
 * Represents the main entrypoint to the Storj network. An uplink connects to a specific satellite
 * and caches connections and resources, allowing one to create sessions delineated by specific
 * access controls.
 */
public class Uplink implements AutoCloseable {

    private io.storj.libuplink.mobile.Uplink uplink;

    /**
     * Creates new {@link Uplink}.
     *
     * @param options an optional list of {@link UplinkOption}
     */
    public Uplink(UplinkOption... options) {
        UplinkOption.UplinkOptions uplinkOptions = UplinkOption.internal(options);
        this.uplink = new io.storj.libuplink.mobile.Uplink(uplinkOptions.config, uplinkOptions.tempDir);
    }

    /**
     * Returns a {@link Project} handle for the given {@link Scope}.
     *
     * @param scope a {@link Scope}
     * @return a {@link Project} handle
     * @throws StorjException in case of error
     */
    public Project openProject(Scope scope) throws StorjException {
        return this.openProject(scope.getSatelliteAddress(), scope.getApiKey());
    }

    /**
     * Returns a {@link Project} handle for the given satellite address and {@link ApiKey}.
     *
     * @param satelliteAddress a satellite address
     * @param apiKey an {@link ApiKey} to access the satellite
     * @return a {@link Project} handle
     * @throws StorjException in case of error
     */
    public Project openProject(String satelliteAddress, ApiKey apiKey) throws StorjException {
        try {
            io.storj.libuplink.mobile.Project project = uplink.openProject(satelliteAddress, apiKey.serialize());
            return new Project(project);
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

    /**
     * Closes the bucket and releases the allocated network resources.
     *
     * @throws StorjException if an error occurs while closing
     */
    public void close() throws StorjException {
        try {
            uplink.close();
        } catch (Exception e) {
            throw ExceptionUtil.toStorjException(e);
        }
    }

}
