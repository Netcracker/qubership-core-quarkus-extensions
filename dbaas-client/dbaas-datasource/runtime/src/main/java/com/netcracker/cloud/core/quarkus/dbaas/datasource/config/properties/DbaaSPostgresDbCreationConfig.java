package com.netcracker.cloud.core.quarkus.dbaas.datasource.config.properties;

import com.netcracker.cloud.dbaas.client.entity.settings.PostgresSettings;
import com.netcracker.cloud.dbaas.common.config.DbaasApiPropertiesConfig;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

import java.util.Map;
import java.util.Optional;

@ConfigMapping(prefix = "quarkus.dbaas.postgresql.api")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface DbaaSPostgresDbCreationConfig {
    /**
     * dbaas.postgresql.api.service.database-settings
     */
    @WithName("service.database-settings")
    Optional<PostgresSettings> serviceDatabaseSettings();

    /**
     * dbaas.postgresql.api.service.physical-database-id
     */
    @WithName("service.physical-database-id")
    Optional<String> servicePhysicalDatabaseId();

    /**
     * dbaas.postgresql.api.tenant.<tenant-id>.*
     */
    @WithName("tenant")
    Map<String, TenantConfig> tenantDbConfiguration();

    /**
     * dbaas.postgresql.api.tenant.database-settings
     */
    @WithName("tenant.database-settings")
    Optional<PostgresSettings> allTenantsDatabaseSettings();

    /**
     * dbaas.postgresql.api.tenant.physical-database-id
     */
    @WithName("tenant.physical-database-id")
    Optional<String> allTenantsPhysicalDatabaseId();

    /**
     * dbaas.postgresql.api.*
     */
    @WithParentName
    DbaasApiPropertiesConfig dbaasApiPropertiesConfig();

    default PostgresDbConfiguration getPostgresDbConfiguration(String tenantId) {
        if (tenantId == null) {
            return new PostgresDbConfiguration(
                    serviceDatabaseSettings(),
                    servicePhysicalDatabaseId()
            );
        }

        Map<String, TenantConfig> tenants = tenantDbConfiguration();
        if (tenants != null) {
            TenantConfig specific = tenants.get(tenantId);
            if (specific != null) {
                return new PostgresDbConfiguration(
                        specific.databaseSettings(),
                        specific.physicalDatabaseId()
                );
            }
        }

        // fallback: get for all tenants
        if (allTenantsDatabaseSettings().isPresent() || allTenantsPhysicalDatabaseId().isPresent()) {
            return new PostgresDbConfiguration(
                    allTenantsDatabaseSettings(),
                    allTenantsPhysicalDatabaseId()
            );
        }

        // final fallback: get for service
        return new PostgresDbConfiguration(
                serviceDatabaseSettings(),
                servicePhysicalDatabaseId()
        );
    }

    interface TenantConfig {

        /**
         * dbaas.postgresql.api.tenant.<tenant-id>.database-settings
         */
        @WithName("database-settings")
        Optional<PostgresSettings> databaseSettings();

        /**
         * dbaas.postgresql.api.tenant.<tenant-id>.physical-database-id
         */
        @WithName("physical-database-id")
        Optional<String> physicalDatabaseId();
    }
}
