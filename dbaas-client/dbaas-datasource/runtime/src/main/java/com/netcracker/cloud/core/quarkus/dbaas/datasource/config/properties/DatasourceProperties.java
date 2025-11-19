package com.netcracker.cloud.core.quarkus.dbaas.datasource.config.properties;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

import java.util.Map;

@ConfigMapping(prefix = "quarkus.dbaas")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface DatasourceProperties {

    /**
     * JDBC config (dbaas.datasource.jdbc).
     */
    @WithName("datasource.jdbc")
    JDBCConfig jdbc();

    /**
     * Enables enhanced leak report (dbaas.datasource.enhanced-leak-report.enable).
     */
    @WithName("datasource.enhanced-leak-report.enable")
    @WithDefault("false")
    boolean enhancedLeakReport();

    /**
     * Enables debug datasource listeners (dbaas.datasource.debug-listener.enable).
     */
    @WithName("datasource.debug-listener.enable")
    @WithDefault("false")
    boolean debugDatasourceListeners();

    /**
     * Global JDBC properties (dbaas.datasource.jdbc-properties.*).
     */
    @WithName("datasource.jdbc-properties")
    Map<String, String> globalJdbcProperties();

    /**
     * Global XA properties (dbaas.datasource.xa-properties.*).
     */
    @WithName("datasource.xa-properties")
    Map<String, String> globalXaProperties();

    /**
     * Global XA flag (dbaas.datasource.xa).
     */
    @WithName("datasource.xa")
    @WithDefault("false")
    boolean xa();

    /**
     * Per-datasource config (dbaas.&lt;datasource-name&gt;.*).
     */
    @WithParentName
    Map<String, JDBCProperties> datasources();

    interface JDBCProperties {

        /**
         * JDBC config for this datasource (dbaas.&lt;name&gt;.jdbc).
         */
        @WithName("jdbc")
        JDBCConfig jdbc();

        /**
         * JDBC properties for this datasource (dbaas.&lt;name&gt;.jdbc-properties.*).
         */
        @WithName("jdbc-properties")
        Map<String, String> jdbcProperties();

        /**
         * XA properties for this datasource (dbaas.&lt;name&gt;.xa-properties.*).
         */
        @WithName("xa-properties")
        Map<String, String> xaProperties();

        /**
         * XA flag for this datasource (dbaas.&lt;name&gt;.xa).
         */
        @WithName("xa")
        @WithDefault("false")
        boolean xa();
    }
}
