package com.netcracker.cloud.dbaas.common.classifier;

import org.qubership.cloud.context.propagation.core.ContextManager;
import org.qubership.cloud.dbaas.client.DbaasConst;
import org.qubership.cloud.dbaas.client.management.DbaasDbClassifier;
import org.qubership.cloud.dbaas.client.management.classifier.DbaaSChainClassifierBuilder;
import org.qubership.cloud.framework.contexts.tenant.TenantContextObject;

import java.util.Map;

import static org.qubership.cloud.dbaas.client.DbaasConst.SCOPE;
import static org.qubership.cloud.dbaas.client.DbaasConst.TENANT_ID;
import static org.qubership.cloud.framework.contexts.tenant.BaseTenantProvider.TENANT_CONTEXT_NAME;

public class TenantClassifierBuilder extends DbaaSChainClassifierBuilder {

    public TenantClassifierBuilder(Map<String, Object> primaryClassifier) {
        super(null);
        getWrapped().withProperties(primaryClassifier);
    }

    @Override
    public DbaasDbClassifier build() {
        return new DbaasDbClassifier.Builder()
                .withProperties(getWrapped().build().asMap())
                .withProperty(SCOPE, DbaasConst.TENANT)
                .withProperty(TENANT_ID, ((TenantContextObject) ContextManager.get(TENANT_CONTEXT_NAME)).getTenant()).build();
    }
}
