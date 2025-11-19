package com.netcracker.cloud.core.quarkus.dbaas.datasource.config.properties;

import com.netcracker.cloud.dbaas.client.entity.settings.PostgresSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostgresDbConfiguration {

    /**
     * Postgres settings (extensions, etc.).
     */
    private Optional<PostgresSettings> databaseSettings;

    /**
     * Physical database id.
     */
    private Optional<String> physicalDatabaseId;
}
